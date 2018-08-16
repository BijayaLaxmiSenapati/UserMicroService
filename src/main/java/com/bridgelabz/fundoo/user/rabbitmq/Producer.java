package com.bridgelabz.fundoo.user.rabbitmq;

import com.bridgelabz.fundoo.user.models.Email;

public interface Producer {
	
	public void produceMsg(Email email);
}
