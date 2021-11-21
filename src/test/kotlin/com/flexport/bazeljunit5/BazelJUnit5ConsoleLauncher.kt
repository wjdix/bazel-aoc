package com.flexport.bazeljunit5

/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

import org.junit.platform.commons.util.ReflectionUtils
import org.junit.platform.console.ConsoleLauncher
import java.lang.reflect.Method
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors
import kotlin.streams.toList
import kotlin.system.exitProcess


/**
 * A ConsoleLauncher to transform a test into JUnit5 fashion for Bazel.
 */
object BazelJUnit5ConsoleLauncher {
    private const val SELECT_PACKAGE = "--select-package"
    private const val SELECT_CLASS = "--select-class"
    private const val SELECT_METHOD = "--select-method"
    /**
     * Transform args and invoke the real implementation.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val exitCode: Int = ConsoleLauncher.execute(System.out, System.err, *transformArgs(args)).exitCode
        System.getenv("XML_OUTPUT_FILE")?.let(XMLOutputFix::execute)
        exitProcess(exitCode)
    }

    /**
     * Transform args into JUnit5 fashion.
     */
    private fun transformArgs(args: Array<String>): Array<String> =
        transformArgsForXmlOutputFile(
            transformArgsForTestBridgeTestOnly(
                listOf(*args), System.getenv("TESTBRIDGE_TEST_ONLY")
            ),
            System.getenv("XML_OUTPUT_FILE")
        ).toTypedArray()

    private fun transformArgsForTestBridgeTestOnly(
        args: List<String>,
        testBridgeTestOnly: String?
    ): List<String> {
        if (testBridgeTestOnly == null || testBridgeTestOnly.isEmpty()) {
            return args
        }

        // When e.g. `bazel test --test_filter=foo //bar:test` is run, the `--test_filter=` value is
        // set in the TESTBRIDGE_TEST_ONLY environment variable by Bazel. For that example, value of the
        // environment variable would simply be `foo`. Here are some examples of what it might look
        // like in practice: test.package.TestClass#testMethod, test.package.TestClass, test.package
        val newArgs: MutableList<String> = ArrayList()
        newArgs.addAll(filterOptions(args, listOf(SELECT_PACKAGE)))
        val fargs = parseOptions(testBridgeTestOnly)
        print(fargs)
        newArgs.addAll(fargs)
        return newArgs
    }

    private fun transformArgsForXmlOutputFile(
        args: List<String>, xmlOutputFile: String?
    ): List<String> {
        if (xmlOutputFile == null || xmlOutputFile.isEmpty()) {
            return args
        }

        // The XML output reporting file is set in the XML_OUTPUT_FILE environment variable by JUnit4.
        // And JUnit5 will get the reports directory from `--reports-dir`.
        val newArgs: MutableList<String> = ArrayList(args)
        newArgs.add("--reports-dir=" + Paths.get(xmlOutputFile).parent.toString())
        return newArgs
    }

    /**
     * Parse JUnit5 option from env.TESTBRIDGE_TEST_ONLY.
     *
     *
     *
     * test.package: --select-package=test.package
     *
     *
     * test.package.TestClass: --select-class=test.package.TestClass
     *
     *
     * test.package.TestClass#testMethod: --select-method=test.package.TestClass#testMethod
     *
     * @param testBridgeTestOnly env.TESTBRIDGE_TEST_ONLY
     * @return option
     */
    private fun parseOptions(testBridgeTestOnly: String): List<String> {
        // transform env.TESTBRIDGE_TEST_ONLY

        // test.package.TestClass#
        val cleanedTestBridgeTestOnly = testBridgeTestOnly
            .removeSuffix("#")
            .removeSuffix("$")

        return when {
            cleanedTestBridgeTestOnly.contains("#") -> parsedFunctionRefenceOptions(cleanedTestBridgeTestOnly)

            isSingleMethodFocus(cleanedTestBridgeTestOnly) -> {
                val className = cleanedTestBridgeTestOnly
                    .split(".")
                    .dropLast(1)
                    .joinToString(".")
                val methodName = cleanedTestBridgeTestOnly.split(".").lastOrNull()
                methodName?.let {
                    val klass = try {
                        Class.forName(className)
                    } catch (e: ClassNotFoundException) {
                        throw IllegalStateException(e)
                    }
                    listOf("$SELECT_METHOD=" + ReflectionUtils
                        .getFullyQualifiedMethodName(klass, methodName))
                }
                    ?: classOrPackage(cleanedTestBridgeTestOnly)
            }
            else -> {
                classOrPackage(cleanedTestBridgeTestOnly)
            }
        }
    }

    @Suppress("SwallowedException")
    private fun classOrPackage(testBridgeTestOnly: String) =
        try {
            Class.forName(testBridgeTestOnly)
            listOf("$SELECT_CLASS=$testBridgeTestOnly")
        } catch (e: ClassNotFoundException) {
            // should be a package
            listOf("$SELECT_PACKAGE=$testBridgeTestOnly")
        }

    private fun isSingleMethodFocus(testBridgeTestOnly: String): Boolean =
        testBridgeTestOnly
            .split(".")
            .lastOrNull()
            ?.firstOrNull()
            ?.isLowerCase()
            ?: false

    fun parsedFunctionRefenceOptions(testBridgeTestOnly: String): MutableList<String> {
        // There could be multiple classes in TESTBRIDGE_TEST_ONLY which are separated by $|
        val splits = testBridgeTestOnly.split("\\$\\|".toRegex()).toTypedArray()
        val methodNames = HashSet<String>()
        val classNames = HashSet<String>()
        for (split in splits) {
            val parts = split.split("#".toRegex()).toTypedArray()
            val className = parts[0]
            classNames.add(className)
            methodNames.addAll(getMethodNames(parts[1]))
        }
        val parsedOptions: MutableList<String> = ArrayList()
        for (className in classNames) {
            val klass: Class<*> = try {
                Class.forName(className)
            } catch (e: ClassNotFoundException) {
                throw IllegalStateException(e)
            }
            parsedOptions.addAll(Arrays.stream(klass.declaredMethods)
                .filter { method: Method ->
                    methodNames.contains(
                        method.name
                    )
                }
                .map { method: Method? ->
                    "$SELECT_METHOD=" + ReflectionUtils
                        .getFullyQualifiedMethodName(klass, method)
                }
                .toList()
            )
        }
        return parsedOptions
    }

    /**
     * bazel --test_filter seems to pass multiple method names in different ways
     * 1. method1,method2,method3
     * 2. (method1|method2|method3)
     *
     *
     * Also in the case of parameterized tests, split on whitespace, '[' or '\' to get only the method name.
     */
    private fun getMethodNames(testFilterMethodsSubstring: String): HashSet<String> {
        var mutableTestFilterMethodsSubstring = testFilterMethodsSubstring
        mutableTestFilterMethodsSubstring = mutableTestFilterMethodsSubstring.replace("(", "").replace(")", "")
        return Arrays.stream(mutableTestFilterMethodsSubstring.split("[,|]".toRegex()).toTypedArray()).distinct()
            .map { s: String ->
                s.split("[ \\[\\\\]".toRegex()).toTypedArray()[0]
            }.collect(
                Collectors.toCollection { HashSet() }
            )
    }

    /**
     * Remove any argument options like:
     *
     *
     * `--select-package=test.package` and `--select-package "test.package"`.
     */
    private fun filterOptions(args: List<String>, excludeOptions: List<String>): List<String> {
        val skipNext = AtomicInteger(0)
        return args.stream()
            .filter { arg: String ->
                if (excludeOptions.contains(arg)) {
                    skipNext.set(1)
                    return@filter false
                }
                if (skipNext.get() == 1) {
                    skipNext.set(0)
                    return@filter false
                }
                !excludeOptions.stream().anyMatch { option: String ->
                    arg.startsWith(
                        "$option="
                    )
                }
            }
            .collect(Collectors.toList())
    }
}
