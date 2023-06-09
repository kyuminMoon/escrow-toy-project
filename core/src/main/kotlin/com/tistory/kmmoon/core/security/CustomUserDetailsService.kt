package com.tistory.kmmoon.core.security

import com.tistory.kmmoon.user.Authority
import com.tistory.kmmoon.user.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findOneWithAuthoritiesByEmail(username) ?: throw UsernameNotFoundException("$username -> 데이터베이스에서 찾을 수 없습니다.")
        return UserSecurity(userEntity.id!!, userEntity.email, userEntity.password, userEntity.authorities.stream()
            .map { auth: Authority -> SimpleGrantedAuthority(auth.authorityName.name) }
            .toList())
    }
}