load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_file")

RULES_JVM_EXTERNAL_TAG = "4.1"

RULES_JVM_EXTERNAL_SHA = "f36441aa876c4f6427bfb2d1f2d723b48e9d930b62662bf723ddfb8fc80f0140"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
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
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2",
    ],
    fetch_sources = True,
    maven_install_json = "@//:maven_install.json",
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()

RULES_KOTLIN_EXTERNAL_TAG = "1.6.0"

rules_kotlin_version = "1.6.0"

rules_kotlin_sha = "a57591404423a52bd6b18ebba7979e8cd2243534736c5c94d35c89718ea38f94"

http_archive(
    name = "io_bazel_rules_kotlin",
    sha256 = rules_kotlin_sha,
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/v%s/rules_kotlin_release.tgz" % rules_kotlin_version],
)

load("@io_bazel_rules_kotlin//kotlin:repositories.bzl", "kotlin_repositories")

kotlin_repositories()  # if you want the default. Otherwise see custom kotlinc distribution below

http_file(
    name = "detekt_cli_jar",
    sha256 = "b20470994a5ffba502c88227377a44f4563df0875dc3f39486e67b7ea4c93c50",
    urls = ["https://jcenter.bintray.com/io/gitlab/arturbosch/detekt/detekt-cli/1.18.1/detekt-cli-1.18.1-all.jar"],
)

rules_detekt_version = "0.6.1"

rules_detekt_sha = "6d532a25caa0d63d142848e7b0d4eb3b2ac89253646b68c1c94b7ec7371bdc1c"

http_archive(
    name = "rules_detekt",
    sha256 = rules_detekt_sha,
    strip_prefix = "bazel_rules_detekt-{v}".format(v = rules_detekt_version),
    url = "https://github.com/buildfoundation/bazel_rules_detekt/archive/v{v}.tar.gz".format(v = rules_detekt_version),
)

register_toolchains("//:kotlin_toolchain")

load("@rules_detekt//detekt:dependencies.bzl", "rules_detekt_dependencies")

rules_detekt_dependencies()

load("@rules_detekt//detekt:toolchains.bzl", "rules_detekt_toolchains")

rules_detekt_toolchains()
