/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.infra.convert;

import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.module.infra.entity.domain.OperatorLogDO;
import org.dromara.visor.module.infra.entity.request.operator.OperatorLogQueryRequest;
import org.dromara.visor.module.infra.entity.vo.LoginHistoryVO;
import org.dromara.visor.module.infra.entity.vo.OperatorLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 操作日志 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Mapper
public interface OperatorLogConvert {

    OperatorLogConvert MAPPER = Mappers.getMapper(OperatorLogConvert.class);

    OperatorLogDO to(OperatorLogModel model);

    OperatorLogDO to(OperatorLogQueryRequest request);

    OperatorLogVO to(OperatorLogDO domain);

    LoginHistoryVO toLoginHistory(OperatorLogDO domain);

}
