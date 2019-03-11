package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class GenController {
	@Autowired
	private FreeMarkerConfig freemarkConfig;
	@RequestMapping("/genhtml")
	@ResponseBody
	public String getHtml() throws Exception {
		Configuration configuration = freemarkConfig.getConfiguration();
		Template template = configuration.getTemplate("hello.ftl");
		// 4、创建数据集
		Map dataModel = new HashMap<>();
		dataModel.put("hello", "1000");
		// 5、创建输出文件的Writer对象。
		Writer out = new FileWriter(new File("E:/freemark/student.html"));
		// 6、调用模板对象的process方法，生成文件。
		template.process(dataModel, out);
		// 7、关闭流。
		out.close();
		return "OK";

	}
}
