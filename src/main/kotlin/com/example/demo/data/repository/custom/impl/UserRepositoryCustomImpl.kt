package com.example.demo.data.repository.custom.impl

import com.example.demo.data.domain.QUser
import com.example.demo.data.domain.User
import com.example.demo.data.repository.custom.UserRepositoryCustom
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class UserRepositoryCustomImpl (
    private val jpaQueryFactory: JPAQueryFactory
) : UserRepositoryCustom {

    override fun userList(): Optional<List<User>> {
        var qUser: QUser = QUser.user

        return Optional.ofNullable(
            jpaQueryFactory.select(qUser)
                .from(qUser)
                .fetch()
        )
    }

}