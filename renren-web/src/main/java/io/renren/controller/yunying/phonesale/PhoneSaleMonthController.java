package io.renren.controller.yunying.phonesale;

import java.io.File;

import static io.renren.utils.ShiroUtils.getUserId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.yunying.phonesale.PhoneSaleMonthEntity;
import io.renren.service.yunying.phonesale.PhoneSaleMonthService;
import io.renren.util.DateUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-15 16:41:23
 */
@Controller
@RequestMapping("phonesalemonth")
public class PhoneSaleMonthController {
	@Autowired
	private PhoneSaleMonthService phoneSaleMonthService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(Integer page, Integer limit, String statMonth){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		if(!StringUtils.isEmpty(statMonth)){
			map.put("statMonth", statMonth.replace("-", ""));
		}
		//查询列表数据
		List<PhoneSaleMonthEntity> phoneSaleMonthList = phoneSaleMonthService.queryList(map);
		int total = phoneSaleMonthService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(phoneSaleMonthList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 每月电销数据导入
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/importPhoneSaleMonthData")
	public R upload(@RequestParam("file") MultipartFile file, String type) {
		try {
			if (file.isEmpty()) {
				throw new RRException("上传文件不能为空");
			}
			String phone_type = "";
			String originalFilename = file.getOriginalFilename();
			if(originalFilename.contains("拉新")){
				phone_type = "laxin";
			}else if(originalFilename.contains("沉默")){
				phone_type = "chenmo";
			}else{
				return R.error("导入文件不正确");
			}
			
			String[] fields = { "number","user_name","real_name","register_time","call_person", "call_date", "call_result"};
			Map<String, Object> retMap = ExcelUtil.parseExcel(multipartToFile(file), null, fields);
			List<Map<String, Object>> list = (List<Map<String, Object>>) retMap.get("list");
			UUID randomUUID = UUID.randomUUID();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				Object markObj = map.get("mark");
				Object callDateObj = map.get("call_date");
				map.put("batch_id", randomUUID.toString());
				map.put("import_user_id", getUserId());
				if(callDateObj != null && !callDateObj.toString().trim().equals("")){
					map.put("stat_month", callDateObj.toString().trim().replace("-", "").replace("//", "").substring(0, 6));
				}
				if(markObj == null || markObj.toString().trim().equals("")){
					map.put("mark", 2);
				}else{
					map.put("mark", markObj);
				}
				map.put("phone_type", phone_type);
			}
			phoneSaleMonthService.batchInsertPhoneSaleMonthData(list);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
		return R.ok();
	}
	
	
	/**
	 * MultipartFile 转换成File
	 * 
	 * @param multfile
	 *            原文件类型
	 * @return File
	 * @throws IOException
	 */
	private File multipartToFile(MultipartFile multfile) throws IOException {
		CommonsMultipartFile cf = (CommonsMultipartFile) multfile;
		// 这个myfile是MultipartFile的
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File file = fi.getStoreLocation();
		// 手动创建临时文件
		if (file.length() < 2048) {
			File tmpFile = new File(
					System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + file.getName());
			multfile.transferTo(tmpFile);
			return tmpFile;
		}
		return file;
	}
	

	/**
	 * 每月电销数据导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/phoneSaleMonthExport")
	public void phoneSaleMonthExport(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		map.put("offset", 0);
		map.put("limit", 1000000);
		Object monthObj = map.get("statMonth");
		if(monthObj != null && !"".equals(monthObj.toString().trim())){
			map.put("statMonth", monthObj.toString().replace("-", ""));
		}
		// 查询列表数据
		List<PhoneSaleMonthEntity> dataList = phoneSaleMonthService.queryList(map);
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put( "userName","用户名");
		headMap.put("mark","是否外包");
		headMap.put("phoneType","电销类型");
		headMap.put("callDate","电销日期");
		headMap.put("callResult","接通结果");
		headMap.put("callPerson","电销人员");
		headMap.put("statMonth","电销月份");
		headMap.put("importTime","导入时间");
		headMap.put("importUserName","导入用户");

		String title = "";
		if(monthObj == null || monthObj.toString().trim().equals("")){
			title = "每月电销数据";
		}else{
			title = "每月电销数据-" + monthObj;
		}
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{userName}")
	public R info(@PathVariable("userName") String userName){
		PhoneSaleMonthEntity phoneSaleMonth = phoneSaleMonthService.queryObject(userName);
		
		return R.ok().put("phoneSaleMonth", phoneSaleMonth);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	public R save(@RequestBody PhoneSaleMonthEntity phoneSaleMonth){
		phoneSaleMonthService.save(phoneSaleMonth);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	public R update(@RequestBody PhoneSaleMonthEntity phoneSaleMonth){
		phoneSaleMonthService.update(phoneSaleMonth);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public R delete(@RequestBody String[] userNames){
		phoneSaleMonthService.deleteBatch(userNames);
		
		return R.ok();
	}
	
}
