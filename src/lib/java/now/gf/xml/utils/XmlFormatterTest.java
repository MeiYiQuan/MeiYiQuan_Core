package now.gf.xml.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;

public class XmlFormatterTest {

	public static void main(String[] args) throws XMLStreamException, IOException {
		String root = "D:\\git-space\\incubator-backend-manager\\";
		List<String> pathList = new ArrayList<>();
		pathList.add(root + "pom.xml");
		pathList.add(root + "src\\main\\webapp\\WEB-INF\\web.xml");
		pathList.add(root + "src\\conf\\java\\logback.xml");
		pathList.add(root + "src\\conf\\java\\spring-dao.cfg.xml");
		pathList.add(root + "src\\conf\\java\\spring-mvc.cfg.xml");
		pathList.add(root + "src\\conf\\java\\spring.cfg.xml");
		pathList.add(root + "src\\conf\\java\\spring-servlet.cfg.xml");
		for (String path : pathList) {
			format(path);
		}
	}

	public static void format(String path) {
		System.out.println("format " + path);
		Reader in = null;
		Writer midbody = null;
		Writer out = null;
		try {
			in = new FileReader(path);
			midbody = new StringWriter();
			new XmlFormatter(2).format(in, midbody);
			out = new FileWriter(path);
			IOUtils.write(midbody.toString(), out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(midbody);
			IOUtils.closeQuietly(out);
		}
	}
}
