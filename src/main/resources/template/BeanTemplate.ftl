package ${packageName};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
<#list otherImportList as importItem>
${importItem}
</#list>

/**
 * ${entityNameNote}Bean
 * 用于控制层展示数据
 * @see ${entityPackage}
 * @author ${author}
 * @date ${date}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "${entityNameNote}实体")
public class ${entityName}Bean implements Serializable {

<#list fieldList as field>
    /**
     * ${field.note}
     */
    @ApiModelProperty(value = "${field.note}")
    private ${field.type} ${field.name};

</#list>
    /**
     * ${entityNameNote} 唯一标识 集合
     */
    @ApiModelProperty(value = "${entityNameNote} 唯一标识 集合")
    private List<String> ${lowEntityName}CodeList;

}
