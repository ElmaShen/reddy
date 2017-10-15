package com.web.service;

import java.util.Date;
import java.util.List;

import com.web.dao.entity.Audio;
import com.web.dao.entity.Sound;
import com.web.dao.model.PageBean;

public interface AudioManageService {

	//音檔管理
	public void updateSound(Sound sound);
	public Sound querySoundById(long id);
	public PageBean querySoundByPage(String year, String mon, String area, String cust, String title, String section, String[] voices,
										Integer second, String tone, String role, String skill, int pageSize, int page);
	public void deleteSound(Sound sound);
	public List<Object[]> querySoundGroupBySec(Date startDate, Date endDate);
	
	
	//照片、影片管理
	public PageBean queryAudioByPage(String keywords, String gname, int pageSize, int page);
	public Audio queryAudioById(long id);
	public List<Audio> queryAudioByGno(int gno);
	public void updateAudio(Audio audio);
	public void deleteAudio(Audio audio);
}
