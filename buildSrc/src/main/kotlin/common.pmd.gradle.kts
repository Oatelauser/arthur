plugins {
    pmd
}

dependencies {
    pmd("com.alibaba.p3c:p3c-pmd:2.1.1")
}

pmd {
    ruleSets = listOf(
        "rulesets/java/ali-comment.xml",
        "rulesets/java/ali-concurrent.xml",
        "rulesets/java/ali-constant.xml",
        "rulesets/java/ali-exception.xml",
        "rulesets/java/ali-flowcontrol.xml",
        "rulesets/java/ali-naming.xml",
        "rulesets/java/ali-oop.xml",
        "rulesets/java/ali-orm.xml",
        "rulesets/java/ali-other.xml",
        "rulesets/java/ali-set.xml"
    )
}