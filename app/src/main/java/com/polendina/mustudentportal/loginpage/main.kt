package com.polendina.mustudentportal.loginpage

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.polendina.mustudentportal.ui.theme.MUStudentPortalTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLogin(
    navController: NavController?
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val coroutineScope = rememberCoroutineScope()
    var selectedUniversity: University by remember { mutableStateOf(University(String(), String(), String(), String())) }
    var creditHourChecked by remember { mutableStateOf(false) }
    var academicYearChecked by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(true) }
    var userName by remember { mutableStateOf(String()) }
    var userPassword by remember { mutableStateOf(String()) }
    var universitySearchBarQuery by remember { mutableStateOf(String()) }
    var searchBarActive by remember { mutableStateOf(false) }
    var universitiesList by remember { mutableStateOf(universities) }
    var universitiesLoaded by remember { mutableStateOf(true) }
    val nationalIdFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    LoginPage(
        userName = userName,
        onUserNameChange = {
            userName = it
        },
        userPassword = userPassword,
        onUserPasswordChange = {
            userPassword = it
        },
        onSignIn = { /*TODO*/ },
        academicYearEnabled = selectedUniversity.academicYear.isNotEmpty(),
        academicYearChecked = academicYearChecked,
        creditHourEnabled = selectedUniversity.creditHour.isNotEmpty(),
        creditHourChecked = creditHourChecked,
        onAcademicYearRadioButtonClicked = {
            academicYearChecked = !academicYearChecked
            if (creditHourChecked) { creditHourChecked = false }
        },
        onCreditHourRadioButtonClicked = {
            creditHourChecked = !creditHourChecked
            if (academicYearChecked) { academicYearChecked = false }
        },
        onSelectUniversity = {
            openBottomSheet = true
            coroutineScope.launch {
                bottomSheetState.show()
            }
        },
        passwordVisibility = passwordVisibility,
        passwordImageVectorOnClick = {
            passwordVisibility = !passwordVisibility
        },
        onNext = {
            passwordFocusRequester.requestFocus()
        },
        onDone = {
            focusManager.clearFocus()
        },
        nationalIdFocusRequester = nationalIdFocusRequester,
        passwordFocusRequester = passwordFocusRequester
    )
    if (openBottomSheet) {
        UniversitiesBottomSheet(
            bottomSheetState = bottomSheetState,
            universities = universitiesList,
            universitySearchBarQuery = universitySearchBarQuery,
            onUniversiteQueryChange = {
                universitySearchBarQuery = it
                universitiesList = universities
            },
            onSearch = {
                searchBarActive = false
                universitiesList = universitiesList.filter { university ->  university.name.contains(it) }
            },
            selectedUniversity = selectedUniversity,
            onSelectingUniversity = {
                selectedUniversity = it
                academicYearChecked = false
                creditHourChecked = false
            },
            onDismissRequest = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    openBottomSheet = false
                }
            },
            searchBarActive = searchBarActive,
            onSearchBarActiveChange = {
                searchBarActive = it
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainLogin() {
    MUStudentPortalTheme() {
        MainLogin(
            navController = null
        )
    }
}