package com.zuiqiang.notice.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.alibaba.fastjson.JSON;
import com.zuiqiang.notice.domain.Notice;
import com.zuiqiang.notice.service.Noticeservice;

/**
 * 公告管理
 * 
 * @author 12645
 *
 */

@Controller
@RequestMapping("/admin/notice") // 公告的前缀地址字段
public class NoticeController {
	@Autowired
	private Noticeservice noticeservice;

	Notice notice = new Notice();

	/**
	 * 公告的查询
	 * 
	 * @param noticeId
	 * @return Notice
	 */
	@GetMapping(value = { "/select" })
	@ResponseBody
	public Notice selectByPrimaryKey() {
		return noticeservice.selectByPrimaryKey();
	}

	/**
	 * 插入公告
	 * 
	 * @param Notice
	 * @return int
	 */
	@PostMapping(value = { "/insertall" })
	@ResponseBody
	public String insertNotice(Notice notice) {

		int in = noticeservice.insert(notice);
		if (in > 0) {

			String json = JSON.toJSONString(notice);
			return json;
		}
		return null;
	}

	/**
	 * 插入只给有值的字段赋值（会对传进来的值做非空判断）
	 * 
	 * @param notice
	 * @return int
	 */

	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {

		// 转换日期
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
	}

	@PostMapping(value = { "insert" }) // insertSelective 对应的 SQL 语句加入了 NULL 检验，只会插入数据不为 null 的字段，
//	                                              而 insert会插入所有字段，会插入 null 数据，     如果定义了表default字段，使用 insert 还是会插入 null 
//	                                               而忽略default , insertSelective 当字段为 null 时会用 default 自动填充
	@ResponseBody
	public int insertSelective(Notice notice) {
		int insertSelective = noticeservice.insertSelective(notice);
		return insertSelective;
	}



	/**
	 * 对注入的字段全部更新
	 * 
	 * @param notice
	 * @return int
	 */

	@PostMapping(value = { "updateall" }) // 对你注入的字段全部更新，将为空的字段在数据库中置为NULL。
	@ResponseBody
	public int updateByPrimaryKey(Notice notice) {

		int updateall = noticeservice.updateByPrimaryKey(notice);
		return updateall;

	}

	/**
	 * 对字段进行判断再更新(如果为Null就忽略更新)
	 * 
	 * @param notice
	 * @return int
	 */
	@PostMapping(value = { "update" })
	@ResponseBody
	public int updateByPrimaryKeySelective(Notice notice) {
		int updatea = noticeservice.updateByPrimaryKeySelective(notice);
		return updatea;

	}

	/**
	 * 刪除公告
	 * 
	 * @param noticeId
	 * @return int
	 */
	@GetMapping(value = { "delete" })
	@ResponseBody
	public int deleteNotice(@RequestParam("noticeId") String noticeId) {
		return noticeservice.deleteByPrimaryKey(Integer.parseInt(noticeId));
	}

	/**
	 * 查询公告分页
	 * 
	 * 测试的url http://localhost:8081/admin/notice/noticeshow
	 * 
	 * @throws IOException
	 */

	@RequestMapping(value = "/noticeshow", method = RequestMethod.GET)
	@ResponseBody
	public String noticeshow(Notice notice, Integer page, Integer rows) throws IOException {
		return noticeservice.showNoticesAll(page, rows);
	}

	/**
	 * 查询公告 之 模糊查询
	 * 
	 * 测试的url
	 * http://localhost:8081/admin/notice/findNoticeByLike?noticeContent=ca&page=2&rows=1
	 * 
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/findNoticeByLike", method = RequestMethod.GET)
	public String findNoticeByLike(String noticeContent, Integer page, Integer rows) {
		return noticeservice.findNoticeByLike(noticeContent, page, rows);

	}

}