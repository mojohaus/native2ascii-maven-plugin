def resource = new File(basedir, "target/classes/i18n/resource_ru.properties").getText("UTF-8")
assert resource.contains('hello=\\u041F\\u0440\\u0438\\u0432\\u0435\\u0442') : "Resource was not converted"

def buildLog = new File(basedir, "build.log").getText("UTF-8")
assert buildLog.contains("Converting '") : "No info log message about conversions"
