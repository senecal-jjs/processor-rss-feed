package web

import model.Profile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class Login {
    @PostMapping
    fun login(
        @RequestBody profile: Profile
    ) {
        data.Profile.getUser(profile.username)
            ?.let {

            }
    }
}