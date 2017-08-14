package io.renren.controller;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.LabelTagManagerEntity;
import io.renren.entity.LabeltagUserEntity;
import io.renren.service.LabelTagManagerService;
import io.renren.service.LabeltagUserService;
import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.renren.utils.ShiroUtils.getUserId;


/**
 * 用户标签表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-13 14:08:32
 */
@Controller
@RequestMapping("labeltaguser")
public class LabeltagUserController {
	@Autowired
	private LabeltagUserService labeltagUserService;
	@Autowired
	private LabelTagManagerService labelTagManagerService;
	@Autowired
	private UserBehaviorService userBehaviorService;
	private  String reportType="用户标签表";
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("labeltaguser:list")
	public R list(@RequestBody Map<String, Object> params){
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		long struUserId= getUserId();
		// 获取用户账号
		String strUsername = labelTagManagerService.querySysUser(struUserId);
		params.put("sysUser",strUsername);

		String labeltagUser = labeltagUserService.queryStrSql(params);

		params.put("strSql",labeltagUser);
		//查询列表数据
		List<LabeltagUserEntity> LabeltagUserList = labeltagUserService.queryList(params);

		return R.ok().put("page", LabeltagUserList);
	};

	/**
	 * 标签类型
	 */
	@ResponseBody
	@RequestMapping("/labelTaglist")
	@RequiresPermissions("labeltaguser:list")
	public R labelTaglist(@RequestBody Map<String, Object> params){


		long struUserId= getUserId();
		// 获取用户账号
		String strUsername = labelTagManagerService.querySysUser(struUserId);
		params.put("sysUser",strUsername);
		//查询列表数据
		List<String> LabeltagUserList = labeltagUserService.queryLabelTagType(params);
		labelTagManagerService.exists(params);
		return R.ok().put("labelTaglist", LabeltagUserList);
	};

	/**
	 * 标签类型
	 */
	@ResponseBody
	@RequestMapping("/labelTaglistdetail")
	@RequiresPermissions("labeltaguser:list")
	public R labelTaglistDetail(@RequestBody Map<String, Object> params){
		long struUserId= getUserId();
		// 获取用户账号
		String strUsername = labelTagManagerService.querySysUser(struUserId);
		params.put("sysUser",strUsername);
		//查询列表数据
		List<LabelTagManagerEntity> LabeltagList = labeltagUserService.queryLabelList(params);
		return R.ok().put("labelList", LabeltagList);
	};

	/**
	 * 获取总数
	 */
	@ResponseBody
	@RequestMapping("/labelTaglisttotal")
	@RequiresPermissions("labeltaguser:list")
	public R labelTagUserTotal(){
		//获取总条数
		int total = labeltagUserService.queryTotal();
		return R.ok().put("total", total);
	};

	@ResponseBody
	@RequestMapping("/partExport")
	@RequiresPermissions("labeltaguser:export")
	public void partExport(HttpServletResponse response) throws IOException {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

		List<LabeltagUserEntity> LabeltagList = labeltagUserService.queryExport();
		JSONArray ja = new JSONArray();

		for(int i = 0 ; i < LabeltagList.size() ; i++) {
			LabeltagUserEntity labeltagUser = new LabeltagUserEntity();
			labeltagUser.setUserId(LabeltagList.get(i).getUserId());
			labeltagUser.setCgUserId(LabeltagList.get(i).getCgUserId());
			labeltagUser.setOldUserId(LabeltagList.get(i).getOldUserId());
			labeltagUser.setPhone(LabeltagList.get(i).getPhone());
			labeltagUser.setUsername(LabeltagList.get(i).getUsername());
			labeltagUser.setRealname(LabeltagList.get(i).getRealname());
			labeltagUser.setRegisterTime(LabeltagList.get(i).getRegisterTime());
			labeltagUser.setFirstinvestTime(LabeltagList.get(i).getFirstinvestTime());
			labeltagUser.setChannelName(LabeltagList.get(i).getChannelName());
			labeltagUser.setSex(LabeltagList.get(i).getSex());
			labeltagUser.setAge(LabeltagList.get(i).getAge());
			labeltagUser.setIsInterflow(LabeltagList.get(i).getIsInterflow());
			labeltagUser.setIsDepository(LabeltagList.get(i).getIsDepository());
			labeltagUser.setInvPeriod(LabeltagList.get(i).getInvPeriod());
			labeltagUser.setInvInterval(LabeltagList.get(i).getInvInterval());
			labeltagUser.setNormalPeriodPreference(LabeltagList.get(i).getNormalPeriodPreference());
			labeltagUser.setChangePeriodPreference(LabeltagList.get(i).getChangePeriodPreference());
			labeltagUser.setCumulativeInvMoney(LabeltagList.get(i).getCumulativeInvMoney());
			labeltagUser.setCumulativeInvMoney(LabeltagList.get(i).getTotalAssets());
			labeltagUser.setCumulativeInvMoneyYear(LabeltagList.get(i).getCumulativeInvMoneyYear());
			labeltagUser.setBalance(LabeltagList.get(i).getBalance());
			labeltagUser.setLastInvMoney(LabeltagList.get(i).getLastInvMoney());
			labeltagUser.setInvMaxMoney(LabeltagList.get(i).getInvMaxMoney());
			labeltagUser.setUseVoucherProportion(LabeltagList.get(i).getUseVoucherProportion());
			labeltagUser.setCumulativeUseVoucherMoney(LabeltagList.get(i).getCumulativeUseVoucherMoney());
			labeltagUser.setCumulativeUseVoucherCou(LabeltagList.get(i).getCumulativeUseVoucherCou());
			labeltagUser.setVoucherEarningsProportion(LabeltagList.get(i).getVoucherEarningsProportion());
			labeltagUser.setCumulativeUsageVoucher(LabeltagList.get(i).getCumulativeUsageVoucher());
			labeltagUser.setAvgUseMoney(LabeltagList.get(i).getAvgUseMoney());
			labeltagUser.setLastInvTime(LabeltagList.get(i).getLastInvTime());
			labeltagUser.setLastRechargeTime(LabeltagList.get(i).getLastRechargeTime());
			labeltagUser.setAvgNormalMoney(LabeltagList.get(i).getAvgNormalMoney());
			labeltagUser.setAvgChangeMoney(LabeltagList.get(i).getAvgChangeMoney());
			labeltagUser.setAvgInvMoney(LabeltagList.get(i).getAvgInvMoney());
			labeltagUser.setNormalCou(LabeltagList.get(i).getNormalCou());
			labeltagUser.setChangeCou(LabeltagList.get(i).getChangeCou());
			labeltagUser.setInvCou(LabeltagList.get(i).getInvCou());
			labeltagUser.setVoucherBalance(LabeltagList.get(i).getVoucherBalance());
			labeltagUser.setMarketingDay(LabeltagList.get(i).getMarketingDay());
			labeltagUser.setLastLoginTime(LabeltagList.get(i).getLastLoginTime());
			labeltagUser.setTotalAssets(LabeltagList.get(i).getTotalAssets());
			
			ja.add(labeltagUser);
		}

		Map<String,String> headMap = new LinkedHashMap<String,String>();
		headMap.put("userId","用户主键");
		headMap.put("cgUserId","存管版ID");
		headMap.put("oldUserId","普通版ID");
		headMap.put("phone","手机号");
		headMap.put("username","用户账号");
		headMap.put("realname","用户姓名");
		headMap.put("registerTime","注册日期");
		headMap.put("firstinvestTime","首投日期");
		headMap.put("channelName","渠道名称");
		headMap.put("sex","性别");
		headMap.put("age","用户年龄段");
		headMap.put("isInterflow","是否注册存管系统");
		headMap.put("isDepository","是否开通存管账号");
		headMap.put("invPeriod","投资时间段");
		headMap.put("invInterval","投资间隔");
		headMap.put("normalPeriodPreference","项目期限偏好");
		headMap.put("changePeriodPreference","债转期限偏好");
		headMap.put("cumulativeInvMoney","累计投资金额");
		
		
		headMap.put("totalAssets","当前待收金额");
		
		
		headMap.put("cumulativeInvMoneyYear","累计投资年化金额");
		headMap.put("balance","账户可用余额");
		headMap.put("lastInvMoney","最近一笔投资金额");
		headMap.put("invMaxMoney","单笔投资最高金额");
		headMap.put("useVoucherProportion","使用优惠投资的比例");
		headMap.put("cumulativeUseVoucherMoney","累计使用的红包及券的金额");
		headMap.put("cumulativeUseVoucherCou","累计使用的红包及券的次数");
		headMap.put("voucherEarningsProportion","红包券收益占比已收收益比例");
		headMap.put("cumulativeUsageVoucher","累计红包使用率");
		headMap.put("avgUseMoney","次均红包金额");
		headMap.put("lastInvTime","最近一次投资时间");
		headMap.put("lastRechargeTime","最近一次充值时间");
		headMap.put("avgNormalMoney","平均每笔项目投资金额");
		headMap.put("avgChangeMoney","平均每笔债转投资金额");
		headMap.put("avgInvMoney","平均每笔综合投资金额");
		headMap.put("normalCou","投资项目次数");
		headMap.put("changeCou","投资债转次数");
		headMap.put("invCou","投资综合次数");
		headMap.put("voucherBalance","账户可用优惠余额");
		headMap.put("marketingDay","距离上次营销天数");
		headMap.put("lastLoginTime","最近一次登录时间");

		String title = "用户标签";


		ExcelUtil.downloadExcelFile(title,headMap,ja,response);
	}
	
	
	

}




