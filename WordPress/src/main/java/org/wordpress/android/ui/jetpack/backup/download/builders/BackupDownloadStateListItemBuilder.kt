package org.wordpress.android.ui.jetpack.backup.download.builders

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dagger.Reusable
import org.wordpress.android.R
import org.wordpress.android.ui.jetpack.backup.download.BackupDownloadErrorTypes
import org.wordpress.android.ui.jetpack.backup.download.BackupDownloadErrorTypes.RemoteRequestFailure
import org.wordpress.android.ui.jetpack.common.CheckboxSpannableLabel
import org.wordpress.android.ui.jetpack.common.JetpackBackupRestoreListItemState.BulletState
import org.wordpress.android.ui.jetpack.common.JetpackBackupRestoreListItemState.FootnoteState
import org.wordpress.android.ui.jetpack.common.JetpackBackupRestoreListItemState.SubHeaderState
import org.wordpress.android.ui.jetpack.common.JetpackListItemState
import org.wordpress.android.ui.jetpack.common.JetpackListItemState.ActionButtonState
import org.wordpress.android.ui.jetpack.common.JetpackListItemState.CheckboxState
import org.wordpress.android.ui.jetpack.common.JetpackListItemState.DescriptionState
import org.wordpress.android.ui.jetpack.common.JetpackListItemState.HeaderState
import org.wordpress.android.ui.jetpack.common.JetpackListItemState.IconState
import org.wordpress.android.ui.jetpack.common.JetpackListItemState.ProgressState
import org.wordpress.android.ui.jetpack.common.providers.JetpackAvailableItemsProvider.JetpackAvailableItem
import org.wordpress.android.ui.jetpack.common.providers.JetpackAvailableItemsProvider.JetpackAvailableItemType
import org.wordpress.android.ui.utils.UiString.UiStringRes
import org.wordpress.android.ui.utils.UiString.UiStringResWithParams
import org.wordpress.android.ui.utils.UiString.UiStringText
import org.wordpress.android.util.toFormattedDateString
import org.wordpress.android.util.toFormattedTimeString
import java.util.Date
import javax.inject.Inject

@Reusable
class BackupDownloadStateListItemBuilder @Inject constructor(
    private val checkboxSpannableLabel: CheckboxSpannableLabel
) {
    fun buildDetailsListStateItems(
        published: Date,
        availableItems: List<JetpackAvailableItem>,
        onCreateDownloadClick: () -> Unit,
        onCheckboxItemClicked: (availableItemType: JetpackAvailableItemType) -> Unit
    ): List<JetpackListItemState> {
        val items = mutableListOf(
                buildIconState(
                        R.drawable.ic_get_app_white_24dp,
                        R.string.backup_download_details_icon_content_description,
                        R.color.success_50),
                buildHeaderState(R.string.backup_download_details_header),
                buildDescriptionState(published, R.string.backup_download_details_description_with_two_parameters),
                buildActionButtonState(
                        titleRes = R.string.backup_download_details_action_button,
                        contentDescRes = R.string.backup_download_details_action_button_content_description,
                        onClick = onCreateDownloadClick),
                buildSubHeaderState(R.string.backup_download_details_choose_items_header)
        )

        val availableItemsListItems: List<CheckboxState> = availableItems.map {
            CheckboxState(
                    availableItemType = it.availableItemType,
                    label = UiStringRes(it.labelResId),
                    labelSpannable = checkboxSpannableLabel.buildSpannableLabel(it.labelResId, it.labelHintResId),
                    checked = true,
                    onClick = { onCheckboxItemClicked(it.availableItemType) }
            )
        }
        items.addAll(availableItemsListItems)
        return items
    }

    fun buildProgressListStateItems(
        progress: Int = 0,
        published: Date,
        onNotifyMeClick: () -> Unit
    ): List<JetpackListItemState> {
        return mutableListOf(
                buildIconState(
                        R.drawable.ic_get_app_white_24dp,
                        R.string.backup_download_progress_icon_content_description,
                        R.color.success_50),
                buildHeaderState(R.string.backup_download_progress_header),
                buildDescriptionState(published, R.string.backup_download_progress_description_with_two_parameters),
                buildProgressState(progress),
                buildActionButtonState(
                        titleRes = R.string.backup_download_progress_action_button,
                        contentDescRes = R.string.backup_download_progress_action_button_content_description,
                        onClick = onNotifyMeClick),
                buildFootnoteState(R.string.backup_download_progress_footnote)
        )
    }

    fun buildCompleteListStateItems(
        published: Date,
        onDownloadFileClick: () -> Unit,
        onShareLinkClick: () -> Unit
    ): List<JetpackListItemState> {
        return listOf(
                buildIconState(
                        R.drawable.ic_cloud_done_white_24dp,
                        R.string.backup_download_complete_icon_content_description,
                        R.color.success_50),
                buildHeaderState(R.string.backup_download_complete_header),
                buildDescriptionState(published, R.string.backup_download_complete_description_with_two_parameters),
                buildActionButtonState(
                        titleRes = R.string.backup_download_complete_download_action_button,
                        contentDescRes = R.string.backup_download_complete_download_action_button_content_description,
                        onClick = onDownloadFileClick),
                buildActionButtonState(
                        titleRes = R.string.backup_download_complete_download_share_action_button,
                        contentDescRes =
                            R.string.backup_download_complete_download_share_action_button_content_description,
                        isSecondary = true,
                        iconRes = R.drawable.ic_share_white_24dp,
                        onClick = onShareLinkClick),
                buildFootnoteState(R.string.backup_download_complete_info)
        )
    }

    fun buildErrorListStateErrorItems(errorType: BackupDownloadErrorTypes, onDoneClick: () -> Unit) = (
        if (errorType == RemoteRequestFailure) buildStatusErrorListStateItems(onDoneClick)
        else buildGenericErrorListStateItems(onDoneClick)
    )

    private fun buildStatusErrorListStateItems(onDoneClick: () -> Unit) = listOf(
            buildHeaderState(R.string.backup_download_status_failure_heading),
            buildBulletState(
                    R.drawable.ic_query_builder_white_24dp,
                    R.string.backup_download_status_bullet_clock_icon_content_desc,
                    R.color.warning_50,
                    R.string.backup_download_status_failure_bullet1),
            buildBulletState(
                    R.drawable.ic_gridicons_checkmark_circle,
                    R.string.backup_download_status_bullet_checkmark_icon_content_desc,
                    R.color.success_50,
                    R.string.backup_download_status_failure_bullet2),
            buildBulletState(
                    R.drawable.ic_gridicons_checkmark_circle,
                    R.string.backup_download_status_bullet_checkmark_icon_content_desc,
                    R.color.success_50,
                    R.string.backup_download_status_failure_bullet3,
                    R.dimen.jetpack_backup_restore_last_bullet_bottom_margin),
            buildActionButtonState(
                    titleRes = R.string.restore_complete_failed_action_button,
                    contentDescRes = R.string.restore_complete_failed_action_button_content_description,
                    onClick = onDoneClick)
    )

    private fun buildGenericErrorListStateItems(onDoneClick: () -> Unit) = listOf(
            buildIconState(
                    R.drawable.ic_cloud_off_white_24dp,
                    R.string.backup_download_complete_failed_icon_content_description,
                    R.color.error_50),
            buildHeaderState(R.string.backup_download_complete_failed_description),
            buildSubHeaderState(
                    R.string.request_failed_message,
                    R.dimen.margin_none,
                    R.dimen.jetpack_backup_restore_sub_header_bottom_margin),
            buildActionButtonState(
                    titleRes = R.string.backup_download_complete_failed_action_button,
                    contentDescRes = R.string.backup_download_complete_failed_action_button_content_description,
                    onClick = onDoneClick)
    )

    private fun buildIconState(
        @DrawableRes iconRes: Int,
        @StringRes contentDescRes: Int,
        @ColorRes colorRes: Int
    ) = IconState(
            icon = iconRes,
            contentDescription = UiStringRes(contentDescRes),
            colorResId = colorRes
    )

    private fun buildHeaderState(@StringRes titleRes: Int) = HeaderState(UiStringRes(titleRes))

    private fun buildDescriptionState(published: Date, @StringRes descRes: Int) = DescriptionState(
            UiStringResWithParams(
                    descRes,
                    listOf(
                            UiStringText(published.toFormattedDateString()),
                            UiStringText(published.toFormattedTimeString())
                    )
            )
    )

    private fun buildActionButtonState(
        @StringRes titleRes: Int,
        @StringRes contentDescRes: Int,
        isSecondary: Boolean = false,
        @DrawableRes iconRes: Int? = null,
        onClick: () -> Unit
    ) = ActionButtonState(
        text = UiStringRes(titleRes),
        contentDescription = UiStringRes(contentDescRes),
        isSecondary = isSecondary,
        iconRes = iconRes,
        onClick = onClick
    )

    private fun buildSubHeaderState(
        @StringRes textResId: Int,
        @DimenRes topMarginResId: Int? = null,
        @DimenRes bottomMarginResId: Int? = null
    ) = SubHeaderState(
                    text = UiStringRes(textResId),
                    itemTopMarginResId = topMarginResId,
                    itemBottomMarginResId = bottomMarginResId)

    private fun buildFootnoteState(@StringRes textRes: Int) = FootnoteState(
            UiStringRes(textRes)
    )

    private fun buildProgressState(progress: Int) = ProgressState(
            progress = progress,
            progressLabel = UiStringResWithParams(
                    R.string.backup_download_progress_label,
                    listOf(UiStringText(progress.toString()))
            )
    )

    private fun buildBulletState(
        @DrawableRes iconRes: Int,
        @StringRes contentDescRes: Int,
        @ColorRes colorRes: Int,
        @StringRes labelRes: Int,
        @DimenRes itemBottomMarginResId: Int? = null
    ) = BulletState(
            icon = iconRes,
            contentDescription = UiStringRes(contentDescRes),
            colorResId = colorRes,
            label = UiStringRes(labelRes),
            itemBottomMarginResId = itemBottomMarginResId
    )
}
