package com.hzlf.sampletest.others;

public class UsedPath {

	// private static String ip = "http://www.3tpi.com:8016/limsApi/";
	// private static String ip = "http://115.231.208.133/api/";
	// private static String ip = "http://111.2.23.176:8081/limsApi/";
	private static String ip = "http://111.2.23.176:8083/api/";

	public static String api_Emp_GET = ip + "Emp";
	public static String api_Login_POST = ip + "Login";
	public static String api_Code_GET = ip + "Code?";
	public static String api_DataUpload_POST = ip + "Apply";
	public static String api_ImgUpload_POST = ip + "Apply/ImgUpload";

	// public static String api_AAQI =
	// "http://122.224.186.159:8016/limsApi/AAQI/";
	public static String api_AAQI = ip + "AAQI/";

	public static String api_Sys_GetAllSource = ip + "Sys/GetAllSource";
	// public static String api_Sys_GetClientByName=ip+"Sys/GetClientByName?";

	// public static String serverurl = ip + "update.xml";
	public static String serverurl = "http://111.2.23.176:8083/update.xml";
}
