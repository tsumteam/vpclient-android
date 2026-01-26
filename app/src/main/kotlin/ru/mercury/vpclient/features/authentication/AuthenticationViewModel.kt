package ru.mercury.vpclient.features.authentication

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.RoomException
import ru.mercury.vpclient.core.RoomSQLiteException
import ru.mercury.vpclient.core.entity.LoginFailure
import ru.mercury.vpclient.core.entity.Password
import ru.mercury.vpclient.core.entity.Username
import ru.mercury.vpclient.core.event.FocusEvent
import ru.mercury.vpclient.core.event.SnackbarErrorEvent
import ru.mercury.vpclient.core.exception.VPClientException
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.VPClientViewModel
import ru.mercury.vpclient.features.authentication.intent.AuthenticationIntent
import ru.mercury.vpclient.features.authentication.model.AuthenticationModel
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.main.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val interactor: Interactor
): VPClientViewModel<AuthenticationIntent, AuthenticationModel>(AuthenticationModel()) {

    init {
        dispatch(AuthenticationIntent.CollectAutofill)
    }

    override fun dispatch(intent: AuthenticationIntent) {
        when (intent) {
            is AuthenticationIntent.CollectAutofill -> {}
            is AuthenticationIntent.EnterUsername -> reduce { it.copy(username = Username(intent.username.filterNot(Char::isWhitespace))) }
            is AuthenticationIntent.EnterPassword -> reduce { it.copy(password = Password(intent.password.filterNot(Char::isWhitespace))) }
            is AuthenticationIntent.LoginClick -> {
                launch { MainEventManager.send(MainRoute()) }
                //launch { interactor.login(stateFlow.value.username.value) }
                /*when {
                    stateFlow.value.username.value.isEmpty() && stateFlow.value.password.value.isEmpty() -> reduce { it.copy(failure = LoginFailure.EmptyUsernameAndPassword) }
                    stateFlow.value.username.value.isEmpty() -> reduce { it.copy(failure = LoginFailure.EmptyUsername) }
                    stateFlow.value.password.value.isEmpty() -> reduce { it.copy(failure = LoginFailure.EmptyPassword) }
                    else -> {
                        val job = launch {
                            interactor.catalogBrands()
                            interactor.login(stateFlow.value.username.value)
                            interactor.auth(stateFlow.value.username, stateFlow.value.password)
                            MainEventManager.send(MainRoute())
                        }
                        reduce { it.copy(authJob = job) }
                    }
                }*/
            }
            is AuthenticationIntent.DismissAttention -> reduce { it.copy(failure = null) }
            is AuthenticationIntent.PasswordVisibilityClick -> reduce { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            is AuthenticationIntent.ClearFocus -> launch { push(FocusEvent.Clear) }
            is AuthenticationIntent.DownFocus -> launch { push(FocusEvent.Down) }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is VPClientException -> reduce { it.copy(authJob = null, failure = LoginFailure.Network(throwable.message)) }
            is RoomException, is RoomSQLiteException -> launch { MainEventManager.send(SnackbarErrorEvent(throwable.message.orEmpty())) }
            else -> super.catch(throwable)
        }
    }
}
