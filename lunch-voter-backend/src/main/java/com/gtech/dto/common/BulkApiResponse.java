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

import com.gtech.utils.AppUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Loi Nguyen
 *
 */
@Getter @NoArgsConstructor(staticName="create")
public final class BulkApiResponse<T> {
    private static final String UNDEFINED_KEY = "UNDEFINED";

    private Map<Object, OperationResult<T>> results = new ConcurrentHashMap<>(32, 0.75f, 5);

    public BulkApiResponse<T> put(Object key, OperationResult<T> result) {
        this.results.put(key == null ? UNDEFINED_KEY : key, result);

        return this;
    }

    // Getters
    public String getVersion() {
        return AppUtils.getAppVersion();
    }

    public LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }
}
