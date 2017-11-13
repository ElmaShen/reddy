package com.web.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.dao.AudioDAO;
import com.web.dao.SoundDAO;
import com.web.dao.entity.Audio;
import com.web.dao.entity.Sound;
import com.web.dao.model.PageBean;
import com.web.service.AudioManageService;

@Service("audioManageService")
public class AudioManageServiceImpl implements AudioManageService {

	@Autowired
    private SoundDAO soundDAO;
	
	@Autowired
    private AudioDAO audioDAO;
	
	
	@Override
	public void updateSound(Sound sound){
		soundDAO.saveOrUpdate(sound);
	}
	
	@Override
	public Sound querySoundById(long id){
		return soundDAO.queryById(id);
	}
	
	@Override
	public PageBean querySoundByPage(String year, String mon, String area, String cust, String title, String section, String[] voices,
									Integer second, String tone, String role, String skill, int pageSize, int page){
		int totalCount = soundDAO.getAllRowCount(year, mon, area, cust, title, section, voices, second, tone, role, skill);    
        int totalPage = PageBean.countTotalPage(pageSize, totalCount);  
        int offset = PageBean.countOffset(pageSize, page); 
        int currentPage = PageBean.countCurrentPage(page);
        List<Sound> list = soundDAO.queryForPage(year, mon, area, cust, title, section, voices, second, tone, role, skill, offset, pageSize);
		
		//Set PageBean
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(pageSize);    
        pageBean.setCurrentPage(currentPage);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(list);
        pageBean.init();
        return pageBean;
	}
	
	@Override
	public int querySoundCount(String[] voices){
		return soundDAO.queryCount(voices);
	}
	
	@Override
	public int deleteSoundByVoice(String voice){
		return soundDAO.deleteByVoice(voice);
	}
	
	@Override
	public void deleteSound(Sound sound){
		soundDAO.delete(sound);
	}
	
	@Override
	public List<Object[]> querySoundGroupBySec(Date startDate, Date endDate){
		return soundDAO.queryGroupBySec(startDate, endDate);
	}
	
	@Override
	public int queryAudioCnt(){
		return audioDAO.queryCount();
	}
	
	@Override
	public PageBean queryAudioByPage(String keywords, String gname, int pageSize, int page){
		int totalCount = audioDAO.getAllRowCount(keywords, gname);    
        int totalPage = PageBean.countTotalPage(pageSize, totalCount);  
        int offset = PageBean.countOffset(pageSize, page); 
        int currentPage = PageBean.countCurrentPage(page);
        List<Audio> list = audioDAO.queryForPage(keywords, gname, offset, pageSize);
		
		//Set PageBean
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(pageSize);    
        pageBean.setCurrentPage(currentPage);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(list);
        pageBean.init();
        return pageBean;
	}
	
	@Override
	public Audio queryAudioById(long id){
		return audioDAO.queryById(id);
	}
	
	@Override
	public List<Audio> queryAudioByGno(int gno){
		return audioDAO.queryByGno(gno);
	}
	
	@Override
	public void updateAudio(Audio audio){
		audioDAO.saveOrUpdate(audio);
	}
	
	@Override
	public void deleteAudio(Audio audio){
		audioDAO.delete(audio);
	}
}
