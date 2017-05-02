package egovframework.airyami.cmm.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.taglibs.standard.tag.common.core.Util;

/**
 * 폼에 있는 변수들을 hidden 태그로 만든다.
 * @author mist
 *
 */
public class MakeHiddenTag extends SimpleTagSupport  {
	private Object varObject;
    private String prefix;
    private String filterStr;
    private String exclude;
    private String exCludeFilter;

    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        JspFragment body = getJspBody();

        StringBuffer msg = new StringBuffer();
        if(body != null) {
            body.invoke(null);
        }
        
        
//        Object orig = this.varObject;
        Map<String, Object> orig = (Map<String, Object>)this.varObject;
        if(this.exclude == null)this.exclude="";
        String[] excludes = this.exclude.split(",");
        if(orig == null)return;
        
//        System.out.println("orig :: " + orig.getClass().getName());
        
        //PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(orig);
		//for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
        for (String name : orig.keySet()) {
            
//        	System.out.println("name :: " + name);
//        	System.out.println("value :: " + orig.get(name));
        	
            //String name = propertyDescriptor.getName();

			if ("class".equals(name)) {
				continue;
			}

			if(this.filterStr != null && !"".equals(this.filterStr)){
				if(name.toUpperCase().indexOf(this.filterStr.toUpperCase()) < 0){
					continue;
				}
			}
			
			if(this.exCludeFilter != null && !"".equals(this.exCludeFilter)){
				if(name.toUpperCase().indexOf(this.exCludeFilter.toUpperCase()) == 0){
					continue;
				}
			}

			if(this.exclude != null && !"".equals(this.exclude)){
				boolean excludeFlag = false;
				for(String param : excludes){
					if(name.toUpperCase().equals(param.toUpperCase())){
						excludeFlag = true;
					}
				}
				if(excludeFlag)continue;
			}
			

			//if (PropertyUtils.isReadable(orig, name)) {

				try {
					//Object value = PropertyUtils.getSimpleProperty(orig, name);
					Object value = orig.get(name);
					// html 태그와 관련된 값을 우회하기 위한 처리 완료
					// 배열일 경우 복수개의 hidden 을 만드는 처리 완료
					
//					System.out.println("value of type :: " + value.getClass().getName());

					if(value == null){
						msg.append("<input type='hidden' name='").append(this.prefix==null?"":this.prefix).append(name).append("'")
						.append(" value='").append(value==null?"":Util.escapeXml(value.toString())).append("' />\n");
					}else if (value.getClass().isArray()) {
						Object[] itemsArray = (Object[]) value;
						for (int i = 0; i < itemsArray.length; i++) {
							Object item = itemsArray[i];
							msg.append(hiddenTagWrite(name,String.valueOf(item)));
						}
					}else if (value instanceof Collection) {
						final Collection optionCollection = (Collection) value;
						for (Iterator it = optionCollection.iterator(); it.hasNext();) {
							Object item = it.next();
							msg.append(hiddenTagWrite(name,String.valueOf(item)));
						}
					}
					else if (value instanceof Map) {
						final Map optionMap = (Map) value;
						for (Iterator it = optionMap.entrySet().iterator(); it.hasNext();) {
							Map.Entry entry = (Map.Entry) it.next();
							msg.append(hiddenTagWrite(name,String.valueOf(optionMap.get(entry))));
						}
					}else{

						msg.append("<input type='hidden' name='").append(this.prefix==null?"":this.prefix).append(name).append("'")
						.append(" value='").append(value==null?"":Util.escapeXml(String.valueOf(value))).append("' />\n");
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.err.println("MakeHiddenTag : " + e.getCause() + "[IllegalAccessException]");
				}
			//}
				

		}

        System.out.println("msg :: " + msg);

        out.print(msg.toString());

    }

    private String hiddenTagWrite(String name,String value){
    	StringBuffer msg = new StringBuffer();
    	msg.append("<input type='hidden' name='").append(this.prefix==null?"":this.prefix).append(name).append("'")
		.append(" value='").append(value==null?"":Util.escapeXml(String.valueOf(value))).append("' >\n");
    	return msg.toString();
    }

    public Object getVar() {
        return varObject;
    }

    public void setVar(Object varObject) {
        this.varObject = varObject;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFilter() {
        return filterStr;
    }

    public void setFilter(String filterStr) {
        this.filterStr = filterStr;
    }

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public String getExCludeFilter() {
		return exCludeFilter;
	}

	public void setExCludeFilter(String exCludeFilter) {
		this.exCludeFilter = exCludeFilter;
	}
	
	
}
