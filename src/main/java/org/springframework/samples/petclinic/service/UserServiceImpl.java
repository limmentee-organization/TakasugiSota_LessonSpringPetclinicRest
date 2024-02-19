package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Role;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public void saveUser(User user) {

		if (user.getRoles() == null || user.getRoles().isEmpty())
			throw new IllegalArgumentException("User must have at least a role set!");//roleがセットされていない場合、引数エラー
		for (Role role : user.getRoles()) {
			if (!role.getName().startsWith("ROLE_"))
				role.setName("ROLE_" + role.getName());//ROLE_から始まるようにする
			if (role.getUser() == null)
				role.setUser(user);//roleにuser情報がなかったらセットする
		}

		userRepository.save(user);//user情報を登録
	}

}
