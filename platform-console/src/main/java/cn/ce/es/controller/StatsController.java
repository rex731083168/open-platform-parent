package cn.ce.es.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ce.es.service.IStatsService;
import cn.ce.platform_service.common.Result;
import io.swagger.annotations.Api;

/**
 *
 * @Title: StatsController.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年9月1日 time下午1:45:06
 *
 **/
@Controller
@RequestMapping("/statistics")
@Api("应用统计")
public class StatsController {

	@Autowired
	@Qualifier("statsService")
	private IStatsService statsService;

	/**
	 * 
	 * @param request
	 * @param response
	 * @param type
	 * @param dateTime
	 * @param apiKey
	 * @param param
	 *            1提供者 2使用者
	 * @param lineOrPie
	 *            0 lin 1 pie
	 * @return
	 */
	@RequestMapping(value = "/statisticsLineChartAndPie", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> statisticsLineChartAndPie(HttpServletRequest request, HttpServletResponse response, String type,
			String dateTime, String apiKey, String param, String lineOrPie) {
		return statsService.getReport(param, dateTime, apiKey, type);
	}
}
