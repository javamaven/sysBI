package io.renren.controller.hr;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.hr.DimStaffEntity;
import io.renren.service.hr.DimStaffService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.util.MapUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-28 10:12:47
 */
@Controller
@RequestMapping("/hr/")
public class DimStaffController {
	@Autowired
	private DimStaffService dimStaffService;
	@Autowired
	DataSourceFactory dataSourceFactory;
	
	private static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 上传文件
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file) {
		int total_record = 0;
		int sucess_record = 0;
		List<String> errorList = new ArrayList<String>();
		String insertSql = "insert into dim_staff values(?,?,?,?,?,?,?,?,?) ";
		String deleteSql = "delete from dim_staff where card_id=? ";
		try {
			if (file.isEmpty()) {
				throw new RRException("上传文件不能为空");
			}
			String[] fields = { "realname", "card_id","phone", "department", "part", "post", "if_boss", "work_time", "leave_time"};
			Map<String, Object> retMap = ExcelUtil.parseExcel(multipartToFile(file), null, fields);
			List<Map<String, Object>> list = (List<Map<String, Object>>) retMap.get("list");
			total_record = list.size();//导入总行数
			for (int i = 0; i < list.size(); i++) {
				try {
					Map<String, Object> map = list.get(i);
					String realname = MapUtil.getValue(map, "realname");
					String cardId = MapUtil.getValue(map, "card_id");
					String phone = MapUtil.getValue(map, "phone");
					String department = MapUtil.getValue(map, "department");
					String part = MapUtil.getValue(map, "part");
					String post = MapUtil.getValue(map, "post");
					String ifBoss = MapUtil.getValue(map, "if_boss");
					String workTime = MapUtil.getValue(map, "work_time");
					String leaveTime = MapUtil.getValue(map, "leave_time");
					Date workTimeDate = null;
					Date leaveTimeDate = null;
					if(StringUtils.isNotEmpty(workTime)){
						java.util.Date parse = dateSdf.parse(workTime);
						workTimeDate = new Date(parse.getTime());
					}
					if(StringUtils.isNotEmpty(leaveTime)){
						java.util.Date parse = dateSdf.parse(leaveTime);
						leaveTimeDate =  new Date(parse.getTime());
					}
					DimStaffEntity dimStaff = new DimStaffEntity(realname, cardId, phone, department, part, post, ifBoss,
							workTime, leaveTime);
					//1。没有则insert
					//2.已存在则update
					if(StringUtils.isEmpty(cardId)){//没有身份证号则不要该条数据
						continue;
					}
					DimStaffEntity entity = dimStaffService.queryObject(cardId);
					if(entity == null){
						dimStaffService.save(dimStaff);
					}else{
						dimStaffService.update(dimStaff);
						new JdbcUtil(dataSourceFactory, "oracle26").execute(deleteSql, cardId);
					}
					
					DimStaffEntity entity_new = dimStaffService.queryObject(cardId);
					
					//插入到41oracle
					new JdbcUtil(dataSourceFactory, "oracle26").execute(insertSql, entity_new.getRealname(), entity_new.getCardId(),
							entity_new.getPhone(), entity_new.getDepartment(), entity_new.getPart(), entity_new.getPost(), entity_new.getIfBoss(),
							entity_new.getWorkTimeDate(), entity_new.getLeaveTimeDate());
					sucess_record++;
					
				} catch (Exception e) {
					errorList.add(e.getMessage());
					e.printStackTrace();
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
		return R.ok().put("total_record", total_record).put("sucess_record", sucess_record).put("error_list", errorList);
	}
	
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
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(Integer page, Integer limit){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		
		//查询列表数据
		List<DimStaffEntity> dimStaffList = dimStaffService.queryList(map);
		int total = dimStaffService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dimStaffList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{realname}")
	public R info(@PathVariable("realname") String realname){
		DimStaffEntity dimStaff = dimStaffService.queryObject(realname);
		
		return R.ok().put("dimStaff", dimStaff);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	public R save(@RequestBody DimStaffEntity dimStaff){
		dimStaffService.save(dimStaff);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	public R update(@RequestBody DimStaffEntity dimStaff){
		dimStaffService.update(dimStaff);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public R delete(@RequestBody String[] realnames){
		dimStaffService.deleteBatch(realnames);
		
		return R.ok();
	}
	
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportExcel(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		JSONArray va = new JSONArray();
		List<DimStaffEntity> dataList = dimStaffService.queryList(map);
		for (int i = 0; i < dataList.size(); i++) {
			DimStaffEntity entity = dataList.get(i);
			va.add(entity);
		}
		
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("realname", "员工姓名");
		headMap.put("cardId", "身份证号");
		headMap.put("phone", "手机");
		
		headMap.put("department", "部门");
		headMap.put("part", "部门序列");
		headMap.put("post", "职位");
		
		headMap.put("ifBoss", "是否副总监及以上职位");
		headMap.put("workTime", "入职时间");
		headMap.put("leaveTime", "离职时间");
		
		String title = "员工信息";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
}
