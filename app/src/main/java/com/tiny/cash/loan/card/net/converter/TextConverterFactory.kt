package com.tiny.cash.loan.card.net.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class TextConverterFactory : Converter.Factory() {
    // 我们只关实现从ResponseBody 到 String 的转换，所以其它方法可不覆盖
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return if (type === String::class.java) {
            Converter { value -> value.string() }
        } else null
        //其它类型我们不处理，返回null就行
    }

    companion object {
        @JvmStatic
        fun newInstance(): TextConverterFactory {
            return TextConverterFactory()
        }
    }
}