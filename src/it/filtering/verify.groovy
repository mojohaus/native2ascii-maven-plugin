def resource = new File(basedir, "target/classes/resource_ru.properties").getText("UTF-8")
assert resource.contains('hello=\\u041F\\u0440\\u0438\\u0432\\u0435\\u0442 world') : "Resource was not converted"
