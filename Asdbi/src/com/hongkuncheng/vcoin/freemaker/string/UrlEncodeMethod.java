package com.hongkuncheng.vcoin.freemaker.string;

import java.util.List;
import java.net.URLEncoder;
import org.springframework.stereotype.Component;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

@Component
public class UrlEncodeMethod implements TemplateMethodModelEx {

	@Override
	public Object exec(List args) throws TemplateModelException {
		return URLEncoder.encode(args.get(0).toString());
	}

}
