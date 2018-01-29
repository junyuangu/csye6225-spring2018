package com.concretepage.dao;
import java.util.List;

import com.concretepage.entity.Article;
import com.concretepage.entity.UserInfo;
public interface IUserInfoDAO {
	UserInfo getActiveUser(String userName);
	List<Article> getAllUserArticles();
}