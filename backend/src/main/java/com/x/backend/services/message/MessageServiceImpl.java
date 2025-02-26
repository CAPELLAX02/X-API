package com.x.backend.services.message;

import com.x.backend.services.email.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageServiceImpl implements EmailService {
}
