/*******************************************************************************
 * Copyright(c) FriarTuck Pte Ltd ("FriarTuck"). All Rights Reserved.
 *
 * This software is the confidential and proprietary information of FriarTuck.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with FriarTuck.
 *
 * FriarTuck MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-
 * INFRINGEMENT. FriarTuck SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 ******************************************************************************/
package com.gtech.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gtech.exception.ApiException;
import com.gtech.utils.AppUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/**
 * @author Loi Nguyen
 *
 */
@Getter 
@NoArgsConstructor(staticName="create")
@JsonInclude(Include.NON_EMPTY)
public class OperationResult<T> {
    private String message;

    @Nullable
    private T data;

    public OperationResult<T> from(Exception ex) {
        return OperationResult.<T>create()
                              .from(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    public OperationResult<T> from(ApiException ex) {
        return OperationResult.<T>create()
                              .withMessage(ex.getMessage());
    }

    public OperationResult<T> succeed() {
        this.message = AppUtils.getMessage("common.success");

        return this;
    }

    public OperationResult<T> withData(T data) {
        this.data = data;

        return this;
    }

    public OperationResult<T> withMessage(String message) {
        this.message = message;

        return this;
    }
}
