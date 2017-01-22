package com.wosai.proname.module.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wosai.proname.module.business.entity.UserBall;
import com.wosai.proname.module.business.entity.WebError;
import com.wosai.proname.module.business.service.UserBallService;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/UserRank")
public class gameUserRankController {

	@Resource
	private UserBallService userBallService;

	@Autowired
	private HttpServletRequest request;

	/**
	 * ���ַ������±���
	 *
	 * @param text
	 *            �ַ���
	 * @param resEncoding
	 *            Դ����
	 * @param newEncoding
	 *            �±���
	 * @return ���±������ַ���
	 */
	public String reEncoding(String text, String resEncoding,
			String newEncoding) {
		String rs = null;
		try {
			if (text == null) {
				return "";
			}
			rs = new String(text.getBytes(resEncoding), newEncoding);
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error >>>>>>>>>>>>>>>>>>>" + getCurrentDate());
			System.out.println(e);
			throw new RuntimeException(e);
		}
		return rs;
	}

	/**
	 * �û��ϴ�
	 */
	@RequestMapping(value = "/userUpload", method = RequestMethod.POST)
	public @ResponseBody Object userUpload(String score, String username,
			String wechatId, String phone) {
		// http://localhost/mvc/UserRank/userUpload.do?CustomerID=11&CompanyName=ss
		Map<String, Object> result = new HashMap<String, Object>();
		try {

			// GET ����,����Ҫת��
			// score = reEncoding(score, "ISO8859-1", "UTF-8");
			// username = reEncoding(username, "ISO8859-1", "UTF-8");
			// wechatId = reEncoding(wechatId, "ISO8859-1", "UTF-8");
			// phone = reEncoding(phone, "ISO8859-1", "UTF-8");

			// System.out.println("username:" + username + " wechatId:" +
			// wechatId
			// + " phone:" + phone);

			UserBall user = new UserBall();
			user.setScore(score);
			user.setName("".equals(username) ? "����" : username);
			user.setNickname("".equals(username) ? "����" : username);
			user.setWechatId(wechatId);
			user.setPhone(phone);
			user.setCreateDate(new Date()); // ע�⣬��������ִ�д�û�����⡣
			// user.setUpdateDate(new Date());
			userBallService.regUser(user);

			result.put("result", "0");// �ɹ�
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Error >>>>>>>>>>>>>>>>>>>" + getCurrentDate());
			System.out.println("userUploa=		username:" + username
					+ " wechatId:" + wechatId + " phone:" + phone);
			result.put("result", "-1");// ʧ��

			try {
				UserBall user = new UserBall();
				user.setScore(score);
				user.setName("".equals(username) ? "����" : username);
				user.setNickname("".equals(username) ? "����" : username);
				user.setWechatId(wechatId);
				user.setPhone(phone);
				user.setEmail(getCurrentDate());
				// user.setCreateDate(new Date()); // ע�⣬��������ִ�д�û�����⡣
				userBallService.regUser(user);

				result.put("result", "0");// �ɹ�
			} catch (Exception e2) {
				System.out.println(e);
				System.out.println("Error >>>>>>>>>>>>>>>>>>>" + getCurrentDate());
				System.out.println("����ʧ��      userUploa=		username:" + username
						+ " wechatId:" + wechatId + " phone:" + phone);
				result.put("result", "-1");// ʧ��
			}
		}
		return result;
	}

	@RequestMapping(value = "/webError", method = RequestMethod.GET)
	public @ResponseBody Object webError(String mes) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			// GET ����,����Ҫת��
			mes = reEncoding(mes, "ISO8859-1", "UTF-8");
			
			WebError webError = new WebError();
			webError.setMes(mes);
			webError.setCreateDate(new Date());
			userBallService.reportWebError(webError);
			result.put("result", "0");// �ɹ�
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Error >>>>>>>>>>>>>>>>>>>" + getCurrentDate());
			System.out.println("WebError:  date:" + getCurrentDate() + " mes:" + mes);
			result.put("result", "-1");// ʧ��

			try {
				WebError webError = new WebError();
				webError.setMes(getCurrentDate() + "|" + mes);
				userBallService.reportWebError(webError);
				result.put("result", "0");// �ɹ�(����ʧ��)
			} catch (Exception e2) {
				System.out.println("Error >>>>>>>>>>>>>>>>>>>"
						+ getCurrentDate());
				System.out.println("WebError:(����ʧ��)  date:" + getCurrentDate()
						+ " mes:" + mes);
				result.put("result", "-1");
			}
		}
		return result;
	}

	/**
	 * ��ȡ��
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getRank", method = RequestMethod.GET)
	public @ResponseBody Object getRank(String score) {
		// http://localhost:8080/mvc/UserRank/getRank.do
		Map<String, Object> items = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			int scoreI = 0;
			if (score != null && !"".equals(score)) {
				scoreI = Integer.parseInt(score);
			}

			// System.out.println("score:" + scoreI);

			long userBallCount = userBallService.getUserBallCount();
			long ulevel = (scoreI == 0) ? userBallCount : userBallService
					.getUserBallRank(scoreI);// ���Ϊ0��,���������ǵ�ǰ�������

			List<UserBall> userBalls = userBallService.getUserBallRank();

			items.put("score", scoreI);// ���γɼ�
			items.put("count", userBallCount);// ������
			items.put("ulevel", userBallCount == ulevel ? ulevel : ulevel + 1);// ��������

			for (int i = 0; i < userBalls.size(); i++) {
				UserBall userBall = userBalls.get(i);
				Map<String, Object> item = new HashMap<String, Object>();
				Map<String, Object> user = new HashMap<String, Object>();
				user.put("user_id", userBall.getId());
				user.put("user_nickname", userBall.getName());
				user.put("user_phone", userBall.getPhone());
				user.put("game_score", userBall.getScore());// ����

				item.put("user", user);
				item.put("ulevel", i + 1);// ����
				list.add(item);
				items.put("items", list);
			}
		} catch (Exception e) {
			System.out.println("Error >>>>>>>>>>>>>>>>>>>" + getCurrentDate());
			System.out.println(e);
		}
		return items;
	}

	/**
	 * ��ȡ�� �û�������������
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getRankByTime", method = RequestMethod.GET)
	public @ResponseBody Object getRankByTime(String start_time, String end_time) {

		Map<String, Object> items = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			/*
			 * int scoreI = 0; if (score != null) { scoreI =
			 * Integer.parseInt(score); }
			 * 
			 * System.out.println("score:" + scoreI);
			 */

			long userBallCount = userBallService.getUserBallCountByTime(
					start_time, end_time);
			// long ulevel = userBallService.getUserBallRank(scoreI);

			// ����ǰ���ļ���
			List<UserBall> userBalls = userBallService.getUserBallRankByTime(
					start_time, end_time);

			// items.put("score", scoreI);// ���γɼ�
			items.put("count", userBallCount);// ������
			// items.put("ulevel", ulevel);// ��������

			for (int i = 0; i < userBalls.size(); i++) {
				UserBall userBall = userBalls.get(i);
				Map<String, Object> item = new HashMap<String, Object>();
				Map<String, Object> user = new HashMap<String, Object>();
				user.put("user_id", userBall.getId());
				user.put("user_phone", userBall.getPhone());
				user.put("user_name", userBall.getName());
				user.put("game_score", userBall.getScore());
				item.put("user", user);
				item.put("ulevel", i + 1);// ����
				list.add(item);
				items.put("items", list);
			}
		} catch (Exception e) {
			System.out.println("Error >>>>>>>>>>>>>>>>>>>" + getCurrentDate());
			System.out.println(e);
		}
		return items;
	}

	@RequestMapping(value = "/toUserRank", method = RequestMethod.GET)
	public String toUserRank() {
		// http://localhost:8080/mvc/UserRank/toUserRank.do
		return "userRank.jsp";
	}

	public Map<String, Object> getTempData() {
		Map<String, Object> items = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> item = new HashMap<String, Object>();
		Map<String, Object> user = new HashMap<String, Object>();
		user.put("user_id", "zhangsan");
		user.put("user_avatar",
				"http://img60.nipic.com/file/20141203/18351197_094125138795_1.jpg");// ͷ��
		user.put("user_sex", "��");
		user.put("user_nickname", "����");
		item.put("user", user);
		item.put("ulevel", 1);// ����
		item.put("game_score", "999");// ����
		list.add(item);

		item = new HashMap<String, Object>();
		user = new HashMap<String, Object>();
		user.put("user_id", "lisi");
		user.put("user_avatar",
				"http://img60.nipic.com/file/20141203/18351197_094125138795_1.jpg");// ͷ��
		user.put("user_sex", "��");
		user.put("user_nickname", "����");
		item.put("user", user);
		item.put("ulevel", 2);// ����
		item.put("game_score", "998");// ����
		list.add(item);

		item = new HashMap<String, Object>();
		user = new HashMap<String, Object>();
		user.put("user_id", "wangwu");
		user.put("user_avatar",
				"http://img60.nipic.com/file/20141203/18351197_094125138795_1.jpg");// ͷ��
		user.put("user_sex", "��");
		user.put("user_nickname", "����");
		item.put("user", user);
		item.put("ulevel", 3);// ����
		item.put("game_score", "997");// ����
		list.add(item);

		items.put("items", list);
		return items;
	}

	@RequestMapping(value = "/getRankByTimeOrign", method = RequestMethod.POST)
	public String getRankByTimeOrign(String start_date, String end_date) {
		// long userBallCount =
		// userBallService.getUserBallCountByDate(start_date, end_date);
		// ����ǰ���ļ���
		List<UserBall> userBalls = userBallService.getUserBallRankByDate(
				start_date, end_date);
		request.setAttribute("userBalls", userBalls);
		request.setAttribute("start_date", start_date);
		request.setAttribute("end_date", end_date);

		if (userBalls.size() == 0) {
			return "viewChartNotInTime.jsp";
		}

		return "viewChart.jsp";
	}

	@RequestMapping(value = "/getRankByTimeOrignAjax", method = RequestMethod.POST)
	public @ResponseBody Object getRankByTimeOrignAjax(String start_date,
			String end_date) {
		return getRankByTimeOrignAjax(start_date, end_date);
	}

	public String getCurrentDate() {
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = simp.format(new Date());
		return dateStr;
	}

}
