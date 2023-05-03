import java.io.*;
import org.codehaus.plexus.util.*;

String resource = FileUtils.fileRead( new File( basedir, "target/classes/native2asciified/included/resource_ru.properties" ) );
if ( resource.indexOf( "hello=\\u041F\\u0440\\u0438\\u0432\\u0435\\u0442" ) < 0 ) {
    throw new RuntimeException( "Resource was not converted" );
}

File file = new File( basedir, "target/classes/native2asciified/not_included_resource_ru.properties" );
if ( file.exists() ) {
    throw new RuntimeException( "Not included resource was converted" );
}

file = new File( basedir, "target/classes/native2asciified/included/excluded_resource_ru.properties" );
if ( file.exists() ) {
    throw new RuntimeException( "Excluded resource was converted" );
}

String buildLog = FileUtils.fileRead( new File( basedir, "build.log" ) );
if ( buildLog.indexOf( "Converting '" ) < 0 ) {
    throw new RuntimeException( "No info log message about conversions" );
}
