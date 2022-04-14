package com.project.shop.lemy.Net;


import com.telpoo.frame.net.BaseNetSupport;

public class NetData {
    public static final int CODE_ERROR_SERVER=-2;
    public static final int CODE_ERROR_NETWORK=-1;
    public static final int CODE_SUCCESS=1;
    public static final int CODE_FALSE=2;
    int code=CODE_SUCCESS;
    String msg="error(101)";
    Object data;
    public void setData(Object data){
        this.data=data;
    }
    public String getMsg(){
        if(code==CODE_ERROR_NETWORK)msg="Không có kết nối internet, vui lòng kiểm tra lại!";
        if(code==CODE_ERROR_SERVER)msg="Không thể kết nối đến máy chủ, vui lòng thử lại!";

        return msg;
    }
    public void setMessage(String msg){

        this.msg = msg;
    }
    public void setFail(String msg){
        this.msg = msg;
        code=CODE_FALSE;
    }

    public int getcode(){
        return code;
    }

    public void setCode(int code){
        this.code=code;
    }

    public void setCode(String strcode){
        try {
            code= Integer.parseInt(strcode);
        }
        catch (NumberFormatException e){
            //Log.e("")
        }
    }

    public void setConnectError(){
        String res = BaseNetSupport.getInstance().method_GET("https://www.google.com");
        if(res==null) code=CODE_ERROR_NETWORK;
        else code=CODE_ERROR_SERVER;
    }

    public void setCodeErrorServer(){
       code=NetData.CODE_ERROR_SERVER;
    }

    public void setSuccess(){
        code=CODE_SUCCESS;
    }

    public void setSuccess(Object data){
        code=CODE_SUCCESS;
        this.data=data;
    }

    public Object getData(){
        return data;
    }
    public String getDataAsString(){
        return data.toString();
    }

    public boolean isSuccess(){
        if(code==CODE_SUCCESS) return true;
        return false;
    }

    public boolean isFailed(){
        if(code==CODE_SUCCESS) return false;
        return true;
    }
}
