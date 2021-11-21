load(
    "@io_bazel_rules_kotlin//kotlin:jvm.bzl",
    upstream_kt_jvm_binary = "kt_jvm_binary",
    upstream_kt_jvm_library = "kt_jvm_library",
    upstream_kt_jvm_test = "kt_jvm_test",
)

load("@rules_detekt//detekt:defs.bzl", "detekt")

JUNIT_PLATFORM_GROUP_ID = "org.junit.platform"
JUNIT_PLATFORM_ARTIFACT_ID_LIST = [
    "junit-platform-commons",
    "junit-platform-console",
    "junit-platform-engine",
    "junit-platform-launcher",
    "junit-platform-suite-api",
]

def kt_jvm_binary(name, srcs, **kwargs):
    upstream_kt_jvm_binary(
        name = name,
        srcs = srcs,
        **kwargs
    )

    if srcs:
        _common_tests(name = name, srcs = srcs)

def kt_jvm_library(name, srcs, **kwargs):
    upstream_kt_jvm_library(
        name = name,
        srcs = srcs,
        **kwargs
    )

    _common_tests(name = name, srcs = srcs)

def kt_jvm_test(name, srcs, main_class = None, size = "small", runtime_deps = [], **kwargs):
    junit_console_args = []
    upstream_kt_jvm_test(
        name = name,
        srcs = srcs,
        size = size,
        args = junit_console_args,
        main_class = main_class or "com.flexport.bazeljunit5.BazelJUnit5ConsoleLauncher",
        runtime_deps = runtime_deps
        + [
            "//src/test/kotlin/com/flexport/bazeljunit5",
        ]
        + [
            _format_maven_jar_dep_name(JUNIT_PLATFORM_GROUP_ID, artifact_id)
            for artifact_id in JUNIT_PLATFORM_ARTIFACT_ID_LIST
        ],
        **kwargs
    )

    _common_tests(name = name, srcs = srcs)

def _common_tests(name, srcs):
  detekt(
        name = "%s_detekt_report" % name,
        srcs = srcs,
        cfgs = ["//config:detekt-config.yml"],
  )


def _format_maven_jar_name(group_id, artifact_id):
    return ("%s_%s" % (group_id, artifact_id)).replace(".", "_").replace("-", "_")

def _format_maven_jar_dep_name(group_id, artifact_id):
  return "@maven//:%s" % _format_maven_jar_name(group_id, artifact_id)