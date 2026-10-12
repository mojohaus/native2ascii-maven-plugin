def resource = new File(basedir, "target/classes/native2asciified/included/resource_ru.properties").getText("UTF-8")
assert resource.contains('hello=\\u041F\\u0440\\u0438\\u0432\\u0435\\u0442') : "Resource was not converted"

assert !new File(basedir, "target/classes/native2asciified/not_included_resource_ru.properties").exists() : "Not included resource was converted"
assert !new File(basedir, "target/classes/native2asciified/included/excluded_resource_ru.properties").exists() : "Excluded resource was converted"

def buildLog = new File(basedir, "build.log").getText("UTF-8")
assert buildLog.contains("Converting '") : "No info log message about conversions"
