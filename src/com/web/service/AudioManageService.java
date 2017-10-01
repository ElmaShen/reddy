package com.web.service;

import java.util.List;

import com.web.dao.entity.Audio;
import com.web.dao.entity.Sound;
import com.web.dao.model.PageBean;

public interface AudioManageService {

	//音檔管理
	public void updateSound(Sound sound);
	public Sound querySoundById(long id);
	public PageBean querySoundByPage(String shYear, String shMon, String shArea, String shCust, String shTitle, String shSection, 
										Integer shSecond, String shTone, String shRole, String shSkill, int pageSize, int page);
	public void deleteSound(Sound sound);
	
	
	//照片、影片管理
	public PageBean queryAudioByPage(String keywords, String gname, int pageSize, int page);
	public Audio queryAudioById(long id);
	public List<Audio> queryAudioByGno(int gno);
	public void updateAudio(Audio audio);
	public void deleteAudio(Audio audio);
}
