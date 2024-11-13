package kw.team.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kw.team.designsystem.component.CosmoAiChatButton
import kw.team.main.component.MainAiChatBottomSheet
import kw.team.main.component.MainBottomAppBar
import kw.team.main.component.MainNavHost
import kw.team.main.model.MainBottomAppBarTab
import kw.team.main.model.MainBottomAppBarTab.HOME

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val messages = mainViewModel.chatLog.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { mainViewModel.aiModels.size })
    var currentBottomSheetTab by remember { mutableStateOf(mainViewModel.aiModels.first()) }
    var isShowBottomSheet by remember { mutableStateOf(false) }
    var currentBottomAppBarTab by remember { mutableStateOf(HOME) }
    var message by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            MainBottomAppBar(
                currentTab = currentBottomAppBarTab,
                tabs = MainBottomAppBarTab.entries,
                onTabClick = { selectedTab ->
                    currentBottomAppBarTab = selectedTab
                },
            )
        },
        floatingActionButton = {
            CosmoAiChatButton(
                onClick = {
                    isShowBottomSheet = true
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            MainNavHost(navController = rememberNavController())

            if (isShowBottomSheet) {
                MainAiChatBottomSheet(
                    onDismissRequest = { isShowBottomSheet = false },
                    onTabClick = { selectedTab ->
                        scope.launch {
                            pagerState.animateScrollToPage(selectedTab.ordinal)
                        }
                        currentBottomSheetTab = selectedTab
                    },
                    onSendMessageClick = {
                        mainViewModel.converseWith(message = message)
                        message = ""
                    },
                    onTextValueChanged = { text ->
                        message = text
                    },
                    bottomSheetState = bottomSheetState,
                    pagerState = pagerState,
                    selectedTab = currentBottomSheetTab,
                    tabs = mainViewModel.aiModels,
                    messages = messages.value,
                    message = message,
                )
            }
        }
    }
}
