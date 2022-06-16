package com.javaex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javaex.service.PhoneService;
import com.javaex.vo.PersonVo;

@Controller
public class PhoneController {

	@Autowired
	private PhoneService phoneService;
	
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST })
	public String List(Model model) {
		System.out.println("PhoneController > list()");
		List<PersonVo> pList = phoneService.getPersonList();
		model.addAttribute("pList", pList);
		return "list";
	}
	
	@RequestMapping(value = "/writeForm", method = {RequestMethod.GET, RequestMethod.POST })
	public String writeForm() {
		return "writeForm";
	}
	
	@RequestMapping(value = "/write", method = {RequestMethod.GET, RequestMethod.POST })
	public String write(@ModelAttribute PersonVo personVo) {
		System.out.println("PhoneBookController > write");
		phoneService.personInsert(personVo);
		return "redirect:/list";
	}
	
	@RequestMapping(value = "/delete/{no},{id}", method = {RequestMethod.GET, RequestMethod.POST })
	public String delete(@PathVariable("no") int no, @PathVariable("id") String id) {
		phoneService.personDelete(no);
		return "redirect:/list";
	}
	/*
	@RequestMapping(value = "/modifyForm/{no}", method = { RequestMethod.GET, RequestMethod.POST })
	public String modifyForm(@PathVariable("no") int no, Model model) {

		PersonVo personVo = phoneService.getPerson(no);
		model.addAttribute("personVo", personVo);
		return "modifyForm";
	}

	// ===================================== 수정 =====================================
	@RequestMapping(value = "/modify", method = { RequestMethod.GET, RequestMethod.POST })
	public String modify(@ModelAttribute PersonVo personVo) {
		
		System.out.println(personVo);

		int count = phoneService.personUpdate(personVo);
		System.out.println(count);

		return "redirect:/list";
	}
	*/
}
