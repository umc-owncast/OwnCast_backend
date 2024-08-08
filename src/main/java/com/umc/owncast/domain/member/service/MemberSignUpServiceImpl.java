package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/*
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberSignUpServiceImpl implements MemberSignUpService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public MemberSignUpResponseDTO signUp(MemberSignUpRequestDTO signUpDto) {
        if (memberRepository.existsByNickname(signUpDto.getNickname())) {
            throw new UserHandler(ErrorCode.NICKNAME_ALREADY_EXIST);
        }
        if (memberRepository.existsByLoginId(signUpDto.getLoginId())) {
            throw new UserHandler(ErrorCode.ID_ALREADY_EXIST);
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        MemberSignUpResponseDTO signDto = MemberSignUpResponseDTO
                .toDto(memberRepository.save(signUpDto
                        .toEntity(encodedPassword)));
        return signDto;
    }
}


 */