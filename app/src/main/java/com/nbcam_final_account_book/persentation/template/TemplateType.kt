package com.nbcam_final_account_book.persentation.template

enum class TemplateType {
    NEW, ADD;

    companion object {
        fun templateType(name: String?): TemplateType? {
            return TemplateType.values().find {
                it.name.uppercase() == name?.uppercase()
            }
        }
    }
}