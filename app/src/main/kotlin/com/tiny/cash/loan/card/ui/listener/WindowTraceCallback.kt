package com.tiny.cash.loan.card.ui.listener

import android.annotation.TargetApi
import android.os.Build
import android.view.*
import android.view.accessibility.AccessibilityEvent

class WindowTraceCallback(var callback: Window.Callback) : Window.Callback {
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return callback.dispatchKeyEvent(event)
    }

    override fun dispatchKeyShortcutEvent(event: KeyEvent): Boolean {
        return callback.dispatchKeyShortcutEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return callback.dispatchTouchEvent(event)
    }

    override fun dispatchTrackballEvent(event: MotionEvent): Boolean {
        return callback.dispatchTrackballEvent(event)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        return callback.dispatchGenericMotionEvent(event)
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean {
        return callback.dispatchPopulateAccessibilityEvent(event)
    }

    override fun onCreatePanelView(featureId: Int): View? {
        return callback.onCreatePanelView(featureId)
    }

    override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
        return callback.onCreatePanelMenu(featureId, menu)
    }

    override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean {
        return callback.onPreparePanel(featureId, view, menu)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        return callback.onMenuOpened(featureId, menu)
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        return callback.onMenuItemSelected(featureId, item)
    }

    override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams) {
        callback.onWindowAttributesChanged(attrs)
    }

    override fun onContentChanged() {
        callback.onContentChanged()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        callback.onWindowFocusChanged(hasFocus)
    }

    override fun onAttachedToWindow() {
        callback.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        callback.onDetachedFromWindow()
    }

    override fun onPanelClosed(featureId: Int, menu: Menu) {
        callback.onPanelClosed(featureId, menu)
    }

    override fun onSearchRequested(): Boolean {
        return callback.onSearchRequested()
    }

    override fun onSearchRequested(searchEvent: SearchEvent): Boolean {
        return false
    }

    override fun onWindowStartingActionMode(callback: ActionMode.Callback): ActionMode? {
        return this.callback.onWindowStartingActionMode(callback)
    }

    override fun onWindowStartingActionMode(callback: ActionMode.Callback, type: Int): ActionMode? {
        return null
    }

    override fun onActionModeStarted(mode: ActionMode) {
        callback.onActionModeStarted(mode)
    }

    override fun onActionModeFinished(mode: ActionMode) {
        callback.onActionModeFinished(mode)
    }
}