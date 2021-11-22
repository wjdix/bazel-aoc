load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_file")

RULES_JVM_EXTERNAL_TAG = "4.1"
RULES_JVM_EXTERNAL_SHA = "f36441aa876c4f6427bfb2d1f2d723b48e9d930b62662bf723ddfb8fc80f0140"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "org.junit.jupiter:junit-jupiter:5.8.1",
        "org.junit.jupiter:junit-jupiter-params:5.8.1",
        "org.junit.platform:junit-platform-launcher:1.8.1",
        "org.junit.platform:junit-platform-console:1.8.1",
        "org.junit.platform:junit-platform-suite-api:1.8.1",
        "com.github.kittinunf.fuel:fuel:2.3.1",
        "org.jetbrains.kotlinx:kotlinx-cli-jvm:0.3.3",
    ],
    fetch_sources = True,
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
    maven_install_json = "@//:maven_install.json",
)

load("@maven//:defs.bzl", "pinned_maven_install")
pinned_maven_install()

RULES_KOTLIN_EXTERNAL_TAG = "v1.5.0-beta-4"
RULES_KOTLIN_EXTERNAL_SHA = "6cbd4e5768bdfae1598662e40272729ec9ece8b7bded8f0d2c81c8ff96dc139d"

http_archive(
    name = "io_bazel_rules_kotlin",
    sha256 = RULES_KOTLIN_EXTERNAL_SHA,
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/%s/rules_kotlin_release.tgz" % RULES_KOTLIN_EXTERNAL_TAG],
)

load("@io_bazel_rules_kotlin//kotlin:repositories.bzl", "kotlin_repositories")

kotlin_repositories()

http_file(
    name = "detekt_cli_jar",
    sha256 = "b20470994a5ffba502c88227377a44f4563df0875dc3f39486e67b7ea4c93c50",
    urls = ["https://jcenter.bintray.com/io/gitlab/arturbosch/detekt/detekt-cli/1.18.1/detekt-cli-1.18.1-all.jar"],
)

RULES_DETEKT_EXTERNAL_TAG = "0.5.0"
RULES_DETEKT_EXTERNAL_SHA = "38e722ab9386e8090109993477f2a2154baa82c8db8f3206ec2b5371408e9252"

http_archive(
    name = "rules_detekt",
    sha256 = RULES_DETEKT_EXTERNAL_SHA,
    strip_prefix = "bazel_rules_detekt-%s" % RULES_DETEKT_EXTERNAL_TAG,
    url = "https://github.com/buildfoundation/bazel_rules_detekt/archive/v%s.zip" % RULES_DETEKT_EXTERNAL_TAG,
)

load("@io_bazel_rules_kotlin//kotlin:repositories.bzl", "kotlin_repositories")
kotlin_repositories()

register_toolchains("//:kotlin_toolchain")

load("@rules_detekt//detekt:dependencies.bzl", "rules_detekt_dependencies")
rules_detekt_dependencies()

load("@rules_detekt//detekt:toolchains.bzl", "rules_detekt_toolchains")
rules_detekt_toolchains()
