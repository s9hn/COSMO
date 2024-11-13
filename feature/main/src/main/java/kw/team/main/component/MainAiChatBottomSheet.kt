package kw.team.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kw.team.ai.model.AiModel
import kw.team.designsystem.theme.CosmoTheme
import kw.team.designsystem.theme.CosmoTheme.colors
import kw.team.designsystem.theme.CosmoTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainAiChatBottomSheet(
    onDismissRequest: () -> Unit,
    onTabClick: (selectedAiModel: AiModel) -> Unit,
    bottomSheetState: SheetState,
    pagerState: PagerState,
    tabs: List<AiModel>,
    selectedTab: AiModel,
    modifier: Modifier = Modifier,
) {
    val currentDensity = LocalDensity.current
    val currentConfiguration = LocalConfiguration.current
    val topPadding = remember {
        with(currentDensity) {
            currentConfiguration.screenHeightDp.dp.toPx() * 0.04
        }
    }

    ModalBottomSheet(
        modifier = modifier.padding(top = topPadding.dp),
        sheetState = bottomSheetState,
        onDismissRequest = onDismissRequest,
        containerColor = colors.onSurface50,
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
        ),
    ) {
        CosmoAiChatBottomSheetTabRow(
            tabs = tabs,
            onTabClick = onTabClick,
            selectedAiModel = selectedTab,
        )
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn {
                    items(listOf("123123", "123123", "123123", "123123")) {
                        Text(text = it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CosmoAiChatBottomSheetTabRow(
    tabs: List<AiModel>,
    onTabClick: (selectedAiModel: AiModel) -> Unit,
    selectedAiModel: AiModel,
    modifier: Modifier = Modifier,
) {
    SecondaryTabRow(
        modifier = modifier,
        divider = {
            Spacer(
                modifier = Modifier
                    .height(height = 0.6.dp)
                    .background(color = colors.onSurface100),
            )
        },
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(selectedTabIndex = selectedAiModel.ordinal)
                    .padding(horizontal = 54.dp),
                height = 2.dp,
                color = colors.primary,
            )
        },
        containerColor = colors.onSurface50,
        selectedTabIndex = selectedAiModel.ordinal,
    ) {
        tabs.forEach { tab ->
            Tab(
                selected = selectedAiModel == tab,
                onClick = { onTabClick(tab) },
            ) {
                Text(
                    text = tab.name,
                    style = typography.headline20M,
                    color = colors.onBackground,
                    modifier = Modifier.padding(vertical = 12.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun CosmoAiChatBottomSheetTabRowPreview() {
    CosmoTheme {
        CosmoAiChatBottomSheetTabRow(
            tabs = AiModel.entries,
            onTabClick = {},
            selectedAiModel = AiModel.entries.first(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CosmoAiChatBottomSheetPreview() {
    CosmoTheme {
        MainAiChatBottomSheet(
            onDismissRequest = {},
            onTabClick = {},
            bottomSheetState = rememberStandardBottomSheetState(),
            pagerState = rememberPagerState(pageCount = { 2 }),
            selectedTab = AiModel.entries.first(),
            tabs = AiModel.entries,
        )
    }
}
