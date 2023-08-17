package com.example.customcomponent.components.editTextComponent

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.example.customcomponent.R
import com.example.customcomponent.components.editTextComponent.enums.EditTextComponentInputType
import com.example.customcomponent.components.editTextComponent.enums.EditTextComponentRightIcon
import com.example.customcomponent.components.editTextComponent.enums.EditTextComponentSize
import com.example.customcomponent.components.editTextComponent.enums.EditTextComponentState
import com.example.customcomponent.utils.gone
import com.example.customcomponent.utils.invisible
import com.example.customcomponent.utils.visible
import com.example.customcomponent.databinding.EditTextComponentBinding

class EditTextComponent(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    constructor(context: Context) : this(context, null)

    private var binding: EditTextComponentBinding
    private var state = EditTextComponentState.DEFAULT_ENABLED
    private var attrsTypedArray: TypedArray? = null
    private var icon: Int? = null
    private var size = EditTextComponentSize.DEFAULT
    private var rightIcon = EditTextComponentRightIcon.VISIBLE
    private var inputType = EditTextComponentInputType.PLAINTEXT
    private var hintText = resources.getString(R.string.edt_hint_text)


    init {
        binding = EditTextComponentBinding.inflate(LayoutInflater.from(context), this)
        getPropertiesFromAttrsTypedArray(attrs)
        setEditTextProperties(state, icon, size, rightIcon, inputType, hintText)
    }

    private fun getPropertiesFromAttrsTypedArray(attrs: AttributeSet?) {
        attrsTypedArray = getAttributes(attrs)

        attrsTypedArray?.getInt(
            R.styleable.EditTextComponent_EdtState,
            STATE_DEFAULT_INDEX
        )?.let {
            state = EditTextComponentState.values()[it]
        }

        attrsTypedArray?.getResourceId(R.styleable.EditTextComponent_icon, 0)?.let {
            if (it != 0) {
                icon = it
            }
        }

        attrsTypedArray?.getInt(R.styleable.EditTextComponent_EdtSize, SIZE_DEFAULT_INDEX)?.let {
            size = EditTextComponentSize.values()[it]
        }

        attrsTypedArray?.getInt(R.styleable.EditTextComponent_rightIcon, ICON_VISIBILITY_INDEX)
            ?.let {
                rightIcon = EditTextComponentRightIcon.values()[it]
            }

        attrsTypedArray?.getInt(R.styleable.EditTextComponent_EdtInputType, INPUT_TYPE_INDEX)
            ?.let {
                inputType = EditTextComponentInputType.values()[it]
            }

        attrsTypedArray?.getString(R.styleable.EditTextComponent_android_hint)?.let {
            hintText = it
        }
    }

    fun getAttributes(attrs: AttributeSet?): TypedArray {
        return context.obtainStyledAttributes(
            attrs,
            R.styleable.EditTextComponent
        )
    }

    fun setEditTextProperties(
        state: EditTextComponentState = this.state,
        icon: Int? = null,
        size: EditTextComponentSize = this.size,
        rightIcon: EditTextComponentRightIcon,
        inputType: EditTextComponentInputType,
        hintText: String? = null

    ) {
        setStates(state)
        setSize(size)
        setRightIconVisibility(rightIcon)
        setInputType(inputType)
        icon?.let {
            binding.iconLeft.setImageResource(it)
        }
        hintText?.let {
            binding.edtText.hint = it
        }
    }

    private fun setInputType(inputType: EditTextComponentInputType) {
        when (inputType) {
            //TODO implementar o input Type E-mail

            EditTextComponentInputType.PLAINTEXT -> binding.edtText.inputType =
                (InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)

            EditTextComponentInputType.PASSWORD -> binding.edtText.transformationMethod =
                PasswordTransformationMethod.getInstance()

            EditTextComponentInputType.EMAIL -> binding.edtText.inputType =
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
    }

    private fun setSize(size: EditTextComponentSize) {
        when (size) {
            EditTextComponentSize.DEFAULT -> setSizeDefault()
        }
    }

    private fun setIconDefault() {
        val iconSize = R.dimen.edit_text_component_size_icon
        setIconSize(binding.iconLeft, iconSize)
        setIconSize(binding.iconRigth, iconSize)
    }

    private fun setStates(state: EditTextComponentState) {
        val textColorId: Int
        val drawableBackgroundId: Int
        val statusIcon: Int?
        val backGroundColor: Int?

        when (state) {
            EditTextComponentState.DEFAULT_ENABLED -> {
                textColorId = R.color.black
                drawableBackgroundId = R.drawable.edit_text_background_default
                backGroundColor = R.color.white
                statusIcon = null
                binding.edtText.isEnabled = true
            }

            EditTextComponentState.SUCCESS -> {
                textColorId = R.color.black
                drawableBackgroundId = R.drawable.edit_text_background_success
                backGroundColor = R.color.white
                statusIcon = R.drawable.ic_success
                binding.edtText.isEnabled = true
            }

            EditTextComponentState.ERROR -> {
                textColorId = R.color.black
                drawableBackgroundId = R.drawable.edit_text_background_error
                backGroundColor = R.color.white
                statusIcon = R.drawable.ic_error
                binding.edtText.isEnabled = true
            }

            EditTextComponentState.DISABLED -> {
                textColorId = R.color.color_gray_4
                drawableBackgroundId = R.drawable.edit_text_background_disable
                backGroundColor = R.color.color_gray_1
                statusIcon = null
                binding.edtText.isEnabled = false
            }
        }

        setProperties(
            textColorId = textColorId,
            backgroundDrawableId = drawableBackgroundId,
            statusIcon = statusIcon,
            backGroundColor = backGroundColor
        )
    }

    private fun setRightIconVisibility(
        rightIcon: EditTextComponentRightIcon
    ) {
        when (rightIcon) {
            EditTextComponentRightIcon.GONE -> binding.iconRigth.gone()

            EditTextComponentRightIcon.VISIBLE -> binding.iconRigth.visible()
        }
    }

    private fun setProperties(
        textColorId: Int,
        backgroundDrawableId: Int? = null,
        statusIcon: Int? = null,
        backGroundColor: Int? = null
    ) {
        binding.edtText.setTextColor(ResourcesCompat.getColor(resources, textColorId, null))

        if (backgroundDrawableId != null) {
            binding.editTextView.background = ResourcesCompat.getDrawable(
                resources,
                backgroundDrawableId,
                null
            )
        }

        if (statusIcon != null) {
            binding.ivStatusIcon.setImageResource(statusIcon)
            binding.ivStatusIcon.visible()
        } else {
            binding.ivStatusIcon.invisible()
        }

        if (backGroundColor != null) {
            binding.edtText.background = ResourcesCompat.getDrawable(
                resources,
                backGroundColor,
                null
            )
        }
    }

    private fun setSizeDefault() {
        setHeightDimension(binding.editTextView, R.dimen.edit_text_component_size_height)
        setIconDefault()
    }

    private fun setIconSize(imageView: ImageView, sizeId: Int) {
        val layoutParams = imageView.layoutParams
        val iconSize = resources.getDimension(sizeId).toInt()
        layoutParams.height = iconSize
        layoutParams.width = iconSize
        imageView.layoutParams = layoutParams
    }

    private fun setHeightDimension(cardView: CardView, sizeId: Int) {
        val size = resources.getDimension(sizeId).toInt()
        val layoutParams = cardView.layoutParams
        layoutParams.height = size
        layoutParams.width = LayoutParams.MATCH_PARENT
        cardView.layoutParams = layoutParams
    }

    companion object {
        private const val STATE_DEFAULT_INDEX = 0
        private const val ICON_VISIBILITY_INDEX = 0
        private const val SIZE_DEFAULT_INDEX = 0
        private const val INPUT_TYPE_INDEX = 0
    }
}