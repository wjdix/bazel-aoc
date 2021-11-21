package com.flexport.bazeljunit5

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object XMLOutputFix {
    // LegacyXmlReportGeneratingListener with junit-jupiter generates this file in `--reports-dir` by
    // default
    // https://github.com/junit-team/junit5/blob/37e0f559277f0065f8057cc465a1e8eb91563af6/junit-platform-reporting/src/main/java/org/junit/platform/reporting/legacy/xml/LegacyXmlReportGeneratingListener.java#L116
    private const val XML_OUTPUT_FILE_PATTERN = "^TEST-.*\\.xml$"

    fun execute(xmlOutputFile: String) {
        if (xmlOutputFile.isEmpty()) {
            return
        }
        val requiredPath = Paths.get(xmlOutputFile)
        val dir = requiredPath.parent
        val files = dir.toFile().listFiles { _: File?, filename: String ->
            filename.matches(
                XML_OUTPUT_FILE_PATTERN.toRegex()
            )
        }.orEmpty().filterNotNull()
        if (files.isEmpty()) {
            System.err.println("The XML output file is not found")
            return
        }
        try {
            val mergedXmlOutput = mergeTestResultXmls(files)
            writeXmlOutputToFile(mergedXmlOutput, requiredPath.toString())
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        } catch (e: TransformerException) {
            e.printStackTrace()
        }
    }

    /**
     * Merges multiple JUnit test result xmls into a single one by grouping <testsuite> from individual
     * files under <testsuites> in the final output file. Useful if ConsoleLauncher generates test result
     * xmls for both 'JUnit Jupiter' and 'JUnit Vintage'
    </testsuites></testsuite> */
    @Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
    private fun mergeTestResultXmls(files: List<File>): Document {
        val xmlDocuments: MutableList<Document> = ArrayList()
        files.forEach{ file ->
            val xmlDocument = loadXmlDocument(file)
            removeParenthesesFromTestCaseNames(xmlDocument)
            xmlDocuments.add(xmlDocument)
        }
        val mergedXmlOutput = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .newDocument()
        val rootElement = mergedXmlOutput.createElement("testsuites")
        mergedXmlOutput.appendChild(rootElement)
        xmlDocuments.forEach { xmlDocument ->
            val testSuites = xmlDocument.getElementsByTagName("testsuite")
            for (i in 0 until testSuites.length) {
                rootElement.appendChild(mergedXmlOutput.importNode(testSuites.item(i), true))
            }
        }
        addEmptyMessageAttributeToNodes(mergedXmlOutput.getElementsByTagName("failure"))
        addEmptyMessageAttributeToNodes(mergedXmlOutput.getElementsByTagName("skipped"))
        return mergedXmlOutput
    }

    @Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
    private fun loadXmlDocument(file: File): Document =
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

    /**
     * Having parentheses in the test case names seems to cause issues in IntelliJ - `jump to source`
     * doesn't work in test explorer. This method simply trims everything following the test method name.
     */
    private fun removeParenthesesFromTestCaseNames(document: Document) {
        val nodeList = document.getElementsByTagName("testcase")
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            val nameAttribute = node.attributes.getNamedItem("name")
            var testCaseName: String? = nameAttribute.nodeValue.split("\\(".toRegex()).toTypedArray()[0]

            // Appends display name to test case name in the case of parameterized tests (a bit hacky)
            val testDisplayName = getTestCaseDisplayName(node)
            if (testDisplayName.trim { it <= ' ' }.startsWith("[")) {
                testCaseName += testDisplayName
            }
            nameAttribute.nodeValue = testCaseName
        }
    }

    /**
     * Adds an empty 'message' attribute to specified nodes in test cases. This was needed to get
     * IntelliJ to show failed/ignored tests correctly.
     */
    private fun addEmptyMessageAttributeToNodes(failureNodes: NodeList) {
        for (i in 0 until failureNodes.length) {
            val node = failureNodes.item(i)
            (node as Element).setAttribute("message", "")
        }
    }

    /**
     * Every <testcase> node in the xml has a <system-out> node which has some additional info including
     * a friendly 'display-name'. This is useful especially for parameterized tests to show the params used
     * in individual runs of the test-case.
    </system-out></testcase> */
    private fun getTestCaseDisplayName(testCase: Node): String {
        val systemOutText = (testCase as Element).getElementsByTagName("system-out").item(0)
            .firstChild.nodeValue
        val displayNameTag = "display-name:"
        var displayName = systemOutText
            .substring(systemOutText.indexOf(displayNameTag) + displayNameTag.length)
        displayName = displayName.replace("\\(,\\)".toRegex(), "")
        return displayName
    }

    @Throws(TransformerException::class)
    private fun writeXmlOutputToFile(xmlDocument: Document, fileName: String) {
        val transformer = TransformerFactory.newInstance().newTransformer()
        val source = DOMSource(xmlDocument)
        val streamResult = StreamResult(File(fileName))
        transformer.transform(source, streamResult)
    }

}
