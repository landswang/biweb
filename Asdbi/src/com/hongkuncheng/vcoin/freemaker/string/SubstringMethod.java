package com.hongkuncheng.vcoin.freemaker.string;

import java.util.List;
import org.springframework.stereotype.Component;

import com.hongkuncheng.vcoin.helper.StringHelper;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

@Component
public class SubstringMethod implements TemplateMethodModelEx {

	@Override
	public Object exec(List args) throws TemplateModelException {
		return StringHelper.substring(args.get(0).toString(), Integer.parseInt(args.get(1).toString()));
	}

}
