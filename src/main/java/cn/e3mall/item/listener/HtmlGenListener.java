package cn.e3mall.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HtmlGenListener implements MessageListener {
	
	@Autowired
	private FreeMarkerConfig freemarkConfig;
	
	@Autowired ItemService itemService;
	
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	

	@Override
	public void onMessage(Message message) {
		// 将消息转换为TextMessage类型
		TextMessage textMessage=(TextMessage)message;
		//获取itemID
		try {
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事物提交
			Thread.sleep(1000);
			//根据ID查询商品信息
			TbItem tbItem = itemService.getItemById(itemId);
			Item item= new Item(tbItem);
			//根据ID查询商品desc
			E3Result e3Result = itemService.findItemDescById(itemId);
			TbItemDesc tbItemDesc = (TbItemDesc)e3Result.getData();
			//创建一个数据集
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", tbItemDesc);
			
			//创建Config对象
			Configuration configuration = freemarkConfig.getConfiguration();
			//获取模板
			Template template = configuration.getTemplate("item.ftl");
			//创建一个输出流指定输出文件的位置及名称
			Writer writer = new FileWriter(HTML_GEN_PATH + itemId + ".html");
			
			//保存文件
			template.process(data, writer);
			
			writer.close();
			
			
		} catch (JMSException | InterruptedException | IOException | TemplateException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	
	
}
