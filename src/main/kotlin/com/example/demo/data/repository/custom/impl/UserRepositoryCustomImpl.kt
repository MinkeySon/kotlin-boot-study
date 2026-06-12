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

    override fun userList(): List<User> {
        var qUser: QUser = QUser.user

        return jpaQueryFactory.select(qUser)
                .from(qUser)
                .fetch()
    }

    override fun existsById(id: String): Boolean {
        var qUser: QUser = QUser.user

        return Optional.ofNullable(jpaQueryFactory.select(qUser)
            .from(qUser)
            .where(qUser.id.eq(id))
            .fetchOne()).isPresent
    }

    override fun getByUserId(id: String): User? {
        var qUser: QUser = QUser.user

        return jpaQueryFactory.select(qUser)
            .from(qUser)
            .where(qUser.id.eq(id))
            .fetchOne()
    }
}