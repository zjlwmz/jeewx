package cn.emay;

import org.apache.commons.lang3.StringUtils;

public class EmojiFilterUtils {

    /**
     * 将emoji表情替换成*
     * 
     * @param source
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source) {
        if(StringUtils.isNotBlank(source)){
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
        }else{
            return source;
        }
    }
    public static void main(String[] arg ){
    	
    	String gdd=filterEmoji("张级阿鲁This ߘ³");
    	System.out.println(gdd);
//        try{
//            String text = "张级阿鲁This ߘ³ is a smiley \uD83C\uDFA6 face\uD860\uDD5D \uD860\uDE07 \uD860\uDEE2 \uD863\uDCCA \uD863\uDCCD \uD863\uDCD2 \uD867\uDD98 ";
//            System.out.println(text);
//            System.out.println(text.length());
//            System.out.println(text.replaceAll("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]", "*"));
//            System.out.println(filterEmoji(text));
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    	
    	
    }
}

