def log = new File(basedir, "build.log").text
def warning = "actually) to convert resources!"

// Maven 3.10 defaults project.build.sourceEncoding to UTF-8, so the platform encoding is no longer used
def (major, minor) = mavenVersion.tokenize('.').take(2)*.toInteger()
if (major == 3 && minor < 10) {
    assert log.contains(warning) : "No warning about the platform encoding in the log"
} else {
    assert !log.contains(warning) : "Maven ${mavenVersion} sets an encoding, but the platform encoding was used"
}
