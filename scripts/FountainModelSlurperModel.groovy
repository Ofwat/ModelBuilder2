import uk.gov.ofwat.fountain.modelbuilder.domain.*


//def f = new File("d:/temp/CR_MDL.xml")
def f = new File("d:/temp/SampleDataNewModel.xml")
//def a = new Audit()
//try{
    Model model
    Set<Year> years
    Set<Audit> audits
    Set<Item> items
    Set<Input> inputs
    Set<Heading> headings    
    Set<ValidationRule> validationRules
    Set<CompanyPage> companyPages
    Set<Page> pages    
    Set<Transfer> transfers
    Set<Text> texts

    def rootNode = new XmlSlurper().parseText(f.text)
    println rootNode[0].name
    //println rootNode[0]
    
    rootNode[0].children.each{
        //println it.name
        println "***" + it.name + "***\n"
        println it.children.size()
        
        switch(it.name){
            /*
            case "modeldetails":
                println "Populating model details"
                model = populateModelDetails(it, it.parent)
                println model
            break
            case "years":
                println "Populating years"
                years = populateYears(it)
            break
            case "audits":
                println "Populating audits"
                audits = populateAudits(it)
            break
            case "items":
                println "Populating items"
                items = populateItems(it)
            break     
            case "inputs":
                println "Populating Inputs"
                inputs = populateInputs(it)
            break       
            case "headings":
                println "populating headings"
                headings = populateHeadings(it)
            break
            case "validationRules":
                println "populating validation rules"
                validationRules = populateValidationRules(it)
            break
            case "companyPages":
                println "populating company pages"
                companyPages = populateCompanyPages(it)
            break 
            case "pages":
                println "Populating pages..."
                pages = populatePages(it)
            break
            case "transfers":
                println "Populating transfers..."
                transfers = populateTransfers(it)
            break
            case "text":
                println "populating texts..."
                texts = populateTexts(it)
            break
            */
            case "pages":
                println "Populating pages..."
                pages = populatePages(it)
            break     
        }        
        
        it = null
    }
    /*
    model.yearss = years
    model.auditss = audits
    model.itemss = items
    model.inputss = inputs
    model.headingss = headings
    model.validationRuless = validationRules
    model.companyPagess = companyPages
    model.pagess = pages   
    model.transferss = transfers
    model.textss = texts
    */

//}catch(Exception e){
//    println e
//}


Set<Text> populateTexts(textsNode){
    Set<Text> texts = new HashSet<Text>()
    textsNode.children.each{
        Text text = populateText(it)
        text.add(text)
    }
    return texts
}

Text populateText(textNode){
    Text text = new Text()
    Set<TextBlock> textBlocks = new HashSet<TextBlock>()
    textNode.children.each{ textNodeChild ->
        switch(textNodeChild.name){
            case "code":
                text.code = textCodeChild.text()
            break   
            case "text-blocks":
                textNodeChild.children.each{ textBlockNode ->
                    TextBlock textBlock = new TextBlock()
                    textBlockNode.children.each{ textBlockNodeChild ->
                        switch(textBlockNodeChild.name){
                            case "description":
                                textBlock.description = textBlockNodeChild.text()
                            break
                            case "version-number":
                                textBlock.versionNumber = textBlockNodeChild.text()                                
                            break
                            case "text-format-code":
                                textBlock.textFormatCode = textBlockNodeChild.text()                                
                            break
                            case "text-type-code":
                                textBlock.textTypeCode = textBlockNodeChild.text()                                
                            break
                            case "retired":
                                textBlock.retired = new Boolean(textBlockNodeChild.text())                                
                            break
                            case "data":
                                textBlock.data = textBlockNodeChild.text()                                
                            break                                                                                                                
                        }
                    }
                    textBlocks.add(textBlock)                        
                }
            break        
        }
    }
    text.textBlocks = textBlocks
    return text
}

Set<Transfer> populateTransfers(transfersNode){
    Set<Transfer> transfers = new HashSet<Transfer>()
    transfersNode.children.each{ transferNode ->
        Transfer transfer = new Transfer()
        transferNode.children.each{ transferNodeChild ->
            //println transferNodeChild
            switch(transferNodeChild.name){
                case "description":
                    transfer.description = transferNodeChild.text()
                break  
                case "transfer-condition":
                    println "populating transfer conditions..."
                    transfer.transferCondition = populateTransferCondition(transferNodeChild)
                break  
                case "transfer-blocks":
                    println "populating transfer blocks..."
                    transfer.transferBlockss = populateTransferBlocks(transferNodeChild)
                break                                  
            }
        } 
        transfers.add(transfer)           
    }
    return transfers
}

//TODO fix this!
//
TransferCondition populateTransferCondition(transferConditionsNode){
    //Set<TransferCondition> transferConditions = new HashSet<TransferCondition>()
    //println transferConditionsNode.children
    TransferCondition transferCondition = new TransferCondition()
    transferConditionsNode.children.each{ transferConditionNode ->
        //println transferConditionNode.name
        
        //transferConditionNode.children.each{ transferConditionNodeChild ->
            switch(transferConditionNode.name){
                case "item-code":
                    transferCondition.itemCode = transferConditionNode.text() 
                break
                case "year-code":
                    transferCondition.yearCode = transferConditionNode.text()
                break
                case "value":
                    transferCondition.value = transferConditionNode.text() 
                break
                case "failure-message":
                    transferCondition.failureMessage = transferConditionNode.text() 
                break                                                
            }
        //}
        
    }
    //transferConditions.add(transferCondition)
    return transferCondition
}

Set<TransferBlock> populateTransferBlocks(transferBlocksNode){
    Set<TransferBlock> transferBlocks = new HashSet<TransferBlock>()
    //println transferBlocksNode
    transferBlocksNode.children.each{ transferBlockNode ->
        TransferBlock transferBlock = new TransferBlock()
        //println transferBlockNode
        transferBlockNode.children.each{ transferBlockNodeChild ->
            switch(transferBlockNodeChild.name){
                case "transfer-block-details":
                    transferBlock.transferBlockDetails = populateTransferBlockDetails(transferBlockNodeChild)    
                break
                case "transfer-block-items":
                    transferBlock.transferBlockItemss = populateTransferBlockItems(transferBlockNodeChild)
                break                
            }
        }
    }
    return transferBlocks
}

Set<TransferBlockItem> populateTransferBlockItems(transferBlockItemsNode){
    println "populating transfer block items"
    Set<TransferBlockItem> transferBlockItems = new HashSet<TransferBlockItem>()
    transferBlockItemsNode.children.each{ transferBlockItemNode ->
        TransferBlockItem transferBlockItem = new TransferBlockItem()    
        Set<YearCode> yearCodes = new HashSet<YearCode>()
        transferBlockItemNode.children.each{ transferBlockItemNodeChild ->
            switch(transferBlockItemNodeChild.name){
                case "item-code":
                    transferBlockItem.itemCode = transferBlockItemNodeChild.text()
                break
                case "company-code":
                    transferBlockItem.companyCode = transferBlockItemNodeChild.text()
                break
                case "year-code":
                    //transferBlockItem.yearCode = transferBlockItemNodeChild.text()
                    YearCode yearCode = new YearCode()
                    yearCode.yearCode = transferBlockItemNodeChild.text()
                    yearCodes.add(yearCode)
                break
            }
        }        
        transferBlockItem.yearCodess = yearCodes
        transferBlockItems.add(transferBlockItem)
    }
    return transferBlockItems
}

TransferBlockDetails populateTransferBlockDetails(transferBlockDetailsNode){
    println "populating transfer block details"
    TransferBlockDetails transferBlockDetails = new TransferBlockDetails()
    transferBlockDetailsNode.children.each{ transferBlockDetailsNodeChild ->
        switch(transferBlockDetailsNodeChild.name){
            case "from-model-code":
                transferBlockDetails.fromModelCode = transferBlockDetailsNodeChild.text()
            break
            case "from-version-model":
                transferBlockDetails.fromVersionModel = transferBlockDetailsNodeChild.text()
            break
            case "from-page-code":
                transferBlockDetails.fromPageCode = transferBlockDetailsNodeChild.text()
            break
            case "to-model-code":
                transferBlockDetails.toModelCode = transferBlockDetailsNodeChild.text()
            break
            case "to-version-code":
                transferBlockDetails.toVersionCode = transferBlockDetailsNodeChild.text()
            break
            case "to-page-code":
                transferBlockDetails.toPageCode = transferBlockDetailsNodeChild.text()
            break
            case "to-macro-code":
                transferBlockDetails.toMacroCode = transferBlockDetailsNodeChild.text()
            break                                                                        
        }
    }
    return transferBlockDetails
}

Set<Page> populatePages(pagesNode){
    Set<Page> pages = new HashSet<Page>()
    pagesNode.children.each{ pageNode ->
        //println pageNode.name
        Page page = new Page()
        pageNode.children.each{ pageNodeChild ->
           //println pageNodeChild.name
            switch(pageNodeChild.name){
                case "pagedetails":
                    PageDetails pageDetails = populatePageDetails(pageNodeChild)
                    page.pageDetail = pageDetails
                break
                case "documents":
                    Set<ModelBuilderDocument> documents = populateDocuments(pageNodeChild)
                    page.documentss = documents
                break
                case "sections":
                    println("Populating sections")
                    Set<Section> sections = populateSections(pageNodeChild)
                    page.sectionss = sections //TODO fix pluralisation
                break
            }
        }
        pages.add(page)
    }
    return pages
}

Set<Section> populateSections(sectionsNode){
    Set<Section> sections = new HashSet<Section>()
    sectionCount = 0
    sectionsNode.children.each{ sectionNode ->
        Section section = new Section()
        println sectionNode.name
        println sectionNode.children
        sectionNode.children.each{ sectionNodeChild ->
            switch(sectionNodeChild.name){
                case "sectiondetails":
                    println("Populating section details")
                    println sectionNodeChild.name + "\$\$\$"
                    SectionDetails sectionDetails = populateSectionDetails(sectionNodeChild)
                    section.sectionDetail = sectionDetails
                break
                case "forms":
                    println("Populating forms")                
                    Set<Form> forms = populateForms(sectionNodeChild)
                    section.formss = forms
                break
                case "documents":
                    println("Populating documents")                
                    Set<ModelBuilderDocument> documents = populateDocuments(sectionNodeChild)
                    section.documentss = documents
                break
                case "lines":
                    println("Populating Lines...")
                    section.liness = populateLines(sectionNodeChild)
                break                                                
            }
        }
        println "Adding a section....${section}" 
        sectionCount++  
        def r = sections.add(section)
        println r
    }
    
    println "^^^^${sections}:${sectionCount}"
    return sections
}

Set<Line> populateLines(linesNode){
    Set<Line> lines = new HashSet<Line>()
    linesNode.each{ lineNode ->
        Line line = new Line()
        lineNode.children.each{ lineNodeChild ->
            switch(lineNodeChild.name){
                case "linedetails":
                    LineDetails lineDetails = new LineDetails()
                    lineNodeChild.children.each{ lineDetailsChildNode ->
                        switch(lineDetailsChildNode.name){
                            case "heading":
                                lineDetails.heading = new Boolean(lineDetailsChildNode.text())
                            break
                            case "code":
                                lineDetails.code = lineDetailsChildNode.text()
                            break  
                            case "description":
                                lineDetails.description = lineDetailsChildNode.text()
                            break
                            case "equation":
                                lineDetails.equation = lineDetailsChildNode.text()
                            break
                            case "linenumber":
                                lineDetails.lineNumber = lineDetailsChildNode.text()
                            break
                            case "ruletext":
                                lineDetails.ruleText = lineDetailsChildNode.text()
                            break
                            case "type":
                                lineDetails.type = lineDetailsChildNode.text()
                            break
                            case "company-type":
                                lineDetails.companyType = lineDetailsChildNode.text()
                            break
                            case "use-confidence-grade":
                                lineDetails.useConfidenceGrade = new Boolean(lineDetailsChildNode.text())
                            break
                            case "validation-rule-code":
                                lineDetails.validationRuleCode = lineDetailsChildNode.text()
                            break
                            case "text-code":
                                lineDetails.textCode = lineDetailsChildNode.text()
                            break
                            case "unit":
                                lineDetails.unit = lineDetailsChildNode.text()
                            break
                            case "decimal-places":
                                lineDetails.decimalPlaces = new Integer(lineDetailsChildNode.text())
                            break
                        }
                    }
                    line.lineDetail = lineDetails
                break
                case "documents":
                    Set<ModelBuilderDocument> docs = populateDocumnets(lineNodeChild)
                    line.documentss = docs
                break
                case "cellrange":
                    CellRange cellRange = new CellRange()
                    lineNodeChild.children.each{ cellRangeNodeChild ->
                        switch(cellRangeNodeChild.name){
                            case "startyear":
                                cellRange.startYear = cellRangeNodeChild.text()
                            break
                            case "endyear":
                                cellRange.endYear = cellRangeNodeChild.text()
                            break
                        }
                    }
                    line.cellRange = cellRange
                break
                case "cells":
                    Set<Cell> cells = new HashSet<Cell>()
                    lineNodeChild.children.each{ cellNode ->
                        Cell cell = new Cell()
                        cellNode.children.each{ cellNodeChild ->
                            switch(cellNodeChild.name){
                                case "code":
                                    cell.code = cellNodeChild.text()
                                break
                                case "year":
                                    cell.year = cellNodeChild.text()                                
                                break
                                case "equation":
                                    cell.equation = cellNodeChild.text()                                
                                break
                                case "type":
                                    cell.type = cellNodeChild.text()                                
                                break
                                case "cgtype":
                                    cell.cgType = cellNodeChild.text()                                
                                break                                                                                                                                
                            }              
                        }
                        cells.add(cell)
                    }
                    line.cells = cells
                break
            }         
        }
        lines.add(line)   
    }
    return lines
}

Set<Form> populateForms(formsNode){
    Set<Form> forms = new HashSet<Form>()
    formsNode.children.each{ formNodesChild ->
        Form form = new Form()
        FormDetails formDetails
        Set<FormHeadingCell> formHeadingsTop
        Set<FormHeadingCell> formHeadingsLeft
        Set<FormCell> formCells
        formNodesChild.children.each{ formNodeChildNode ->
            switch(formNodeChildNode.name){
                case "form-details":
                    println("Populating Form Details")
                    formDetails = populateFormDetails(formNodeChildNode)    
                break
                case "form-headings-top":
                    println("Populating Form headings top")                
                    formHeadingsTop = populateFormHeadings(formNodeChildNode)
                break
                case "form-headings-left":
                    println("Populating Form headings left")                
                    formHeadingsLeft = populateFormHeadings(formNodeChildNode)                
                break
                case "form-cells":
                    println("Populating Form cells")                
                    fromCells = populateFormCells(formNodeChildNode)
                break                                                
            }        
        }        
        form.formDetail = formDetails
        form.formHeadingsLefts = formHeadingsLeft
        form.formHeadingsTops = formHeadingsTop
        form.formCellss = formCells
        forms.add(form)
    }
    return forms
}

Set<FormCell> populateFormCells(formCellsNode){
    Set<FormCell> cells = new HashSet<FormCell>()
    formCellsNode.children.each{ formCellNode ->
        FormCell cell = new FormCell()
        formCellNode.children.each{
            switch(it.name){
                case "cell-code":
                    cell.cellCode = formCellNode.text()
                break
                case "use-confidence-Grade":
                    cell.useConfidenceGrade = formCellNode.text()
                break
                case "input-confidence-grade":
                    cell.inputConfidenceGrade = formCellNode.text()
                break
                case "confidence-grade-input-code":
                    cell.confidenceGradeInputCode = formCellNode.text()
                break
                case "row-heading-source":
                    cell.rowHeadingSource = formCellNode.text()
                break
                case "column-heading-source":
                    cell.columnHeadingSource = formCellNode.text()
                break
                case "group-description-code":
                    cell.groupDescriptionCode = formCellNode.text()
                break
                case "row":
                    cell.row = formCellNode.text()
                break
                case "column":
                    cell.column = formCellNode.text()
                break
                case "row-span":
                    cell.rowSpan = formCellNode.text()
                break
                case "column-span":
                    cell.columnSpan = formCellNode.text()
                break
                case "width":
                    cell.width = formCellNode.text()
                break                                                                                                                                                                                
            }    
        }
        cells.add(cell)
    }
    return cells
}

Set<FormHeadingCell> populateFormHeadings(formHeadingsNode){
    Set<FormHeadingCell> formHeadingCells = new HashSet<FormHeadingCell>()
    formHeadingsNode.children.each{ formHeadingsNodeChild ->
        //println formHeadingsNodeChild.children
        FormHeadingCell formHeadingCell = new FormHeadingCell()
        formHeadingsNodeChild.children.each{ formHeadingCellNode ->
            //println formHeadingCellNode.name
            
            switch(formHeadingCellNode.name){
                case "text":
                   formHeadingCell.text = formHeadingCellNode.text()
                break     
                case "use-line-number":
                   formHeadingCell.useLineNumber = new Boolean(formHeadingCellNode.text())
                break  
                case "use-item-code":
                    formHeadingCell.useItemCode = new Boolean(formHeadingCellNode.text())
                break  
                case "use-item-description":
                    formHeadingCell.useItemDescription = new Boolean(formHeadingCellNode.text())
                break  
                case "use-unit":
                    formHeadingCell.useUnit = new Boolean(formHeadingCellNode.text())
                break  
                case "use-year-code":
                    formHeadingCell.useYearCode = new Boolean(formHeadingCellNode.text())
                break  
                case "use-confidence-grade":
                    formHeadingCell.useConfidenceGrade = new Boolean(formHeadingCellNode.text())
                break  
                case "row":
                    formHeadingCell.row = formHeadingCellNode.text()
                break  
                case "column":
                    formHeadingCell.column = formHeadingCellNode.text()
                break  
                case "row-span":
                    formHeadingCell.rowSpan = formHeadingCellNode.text()
                break  
                case "column-span":
                    formHeadingCell.columnSpan = formHeadingCellNode.text()
                break  
                case "item-code":
                    formHeadingCell.itemCode = formHeadingCellNode.text()
                break  
                case "year-code":
                    formHeadingCell.yearCode = formHeadingCellNode.text()
                break  
                case "width":
                    formHeadingCell.width = formHeadingCellNode.text()
                break  
                case "cell-code":
                    formHeadingCell.cellCode = formHeadingCellNode.text()
                break  
                case "group-description-code":
                    formHeadingCell.groupDescriptionCode = formHeadingCellNode.text()
                break                                                                                                                                                                                                                                                                                                                         
            }        
        }             
        formHeadingCells.add(formHeadingCell)
    }
    return formHeadingCells
}

FormDetails populateFormDetails(formDetailsNode){
    FormDetails formDetails = new FormDetails()
    formDetailsNode.children.each{
        switch(it.name){
            case "company-type":
                formDetails.companyType = it.text()
            break
        }
    }
    return formDetails    
}

SectionDetails populateSectionDetails(sectionDetailsNode){
    SectionDetails sectionDetails = new SectionDetails()
    println("Populating Section Details")
    //println sectionDetailsNode.children
    sectionDetailsNode.children.each{ sectionDetailsNodeChild->
        println sectionDetailsNodeChild.name
        switch(sectionDetailsNodeChild.name){
            case "display":
                sectionDetails.display = sectionDetailsNodeChild.text()
            break
            case "code":
                sectionDetails.code= sectionDetailsNodeChild.text()
            break
            case "grouptype":
                sectionDetails.groupType = sectionDetailsNodeChild.text()
            break
            case "use-line-number":
                sectionDetails.useLineNumber = new Boolean(sectionDetailsNodeChild.text())
            break
            case "use-item-code":
                sectionDetails.useItemCode = new Boolean(sectionDetailsNodeChild.text())
            break
            case "use-item-description":
                sectionDetails.useItemDescription = new Boolean(sectionDetailsNodeChild.text())
            break
            case "use-unit":
                sectionDetails.useUnit = new Boolean(sectionDetailsNodeChild.text())
            break
            case "use-year-code":
                sectionDetails.useYearCode = new Boolean(sectionDetailsNodeChild.text())
            break
            case "use-confidence-grades":
                sectionDetails.useConfidenceGrades = new Boolean(sectionDetailsNodeChild.text())
            break
            case "allow-group-selection":
                sectionDetails.allowGroupSelection = new Boolean(sectionDetailsNodeChild.text())
            break
            case "allow-data-changes":
                sectionDetails.allowDataChanges = new Boolean(sectionDetailsNodeChild.text())
            break
            case "section-type":
                sectionDetails.sectionType = sectionDetailsNodeChild.text()
            break
            case "item-code-column":
                sectionDetails.itemCodeColumn = new Integer(sectionDetailsNodeChild.text())
            break                                                                                                                                                
        }
    }
    return sectionDetails
}

Set<ModelBuilderDocument> populateDocuments(documentsNode){
    Set<ModelBuilderDocument> documents = new HashSet<ModelBuilderDocument>()
    documentsNode.children.each{ documentNode -> 
        ModelBuilderDocument document = new ModelBuilderDocument()
        documentNode.children.each{ documentNodeChild ->
            switch(documentNodeChild.name){
                case "reporter":
                    document.reporter = documentNodeChild.text()
                break;
                case "auditor":
                    document.auditor = documentNodeChild.text()                    
                break;                
            }
        } 
        documents.add(document)   
    }
    return documents
}

PageDetails populatePageDetails(pageDetailsNode){
    PageDetails pageDetails = new PageDetails()
    pageDetailsNode.children.each{ pageDetailsNodeChild ->
        switch(pageDetailsNodeChild.name){
            case "code":
                pageDetails.code = pageDetailsNodeChild.text()
            break
            case "description":
                pageDetails.description = pageDetailsNodeChild.text()            
            break
            case "text":
                pageDetails.text = pageDetailsNodeChild.text()            
            break
            case "company-type":
                pageDetails.companyType = pageDetailsNodeChild.text()            
            break
            case "heading":
                pageDetails.heading = pageDetailsNodeChild.text()            
            break
            case "commercial-in-confidence":
                pageDetails.commercialInConfidence = new Boolean(pageDetailsNodeChild.text())            
            break
            case "hidden":
                pageDetails.hidden = new Boolean(pageDetailsNodeChild.text())            
            break
            case "text-code":
                pageDetails.textCode = pageDetailsNodeChild.text()            
            break                                                                                    
        }
    }
    return pageDetails
}


Set<CompanyPage> populateCompanyPages(companyPagesNode){
    Set<CompanyPage> companyPages = new HashSet<CompanyPage>()
    companyPagesNode.children.each{companyPagesNodeChild ->
        CompanyPage companyPage = new CompanyPage()
        companyPagesNodeChild.each{ companyPageNode ->
           companyPageNode.children.each{ companyPageNodeChild ->
               switch(companyPageNodeChild.name){
                   case "company-code":
                       companyPage.companyCode = companyPageNodeChild.text()
                   break
                   case "page-code":
                       companyPage.pageCode = companyPageNodeChild.text()
                   break
               }    
           } 
        }
        companyPages.add(companyPage)
    }
    return companyPages
}


    Set<ValidationRule> populateValidationRules(validationRulesNode){
        //println "Populating validation rules"
        println validationRulesNode.name
        Set<ValidationRule> validationRules = new HashSet<ValidationRule>()
        validationRulesNode.children.each{ validationRulesChildNode ->
            println validationRulesChildNode.name + "AAA"
            //validationRulesChildNode.each{ validationRuleChildNode ->
                println validationRulesChildNode.name + "YYY"
                ValidationRule validationRule = new ValidationRule()
                ValidationRuleDetails validationRuleDetails
                Set<ValidationRuleItem> validationRuleItems                
                //validationRulesChildNode.children.each{ validationRuleNode ->
                    def validationRulesDetailNode = validationRulesChildNode.children[0]
                    println validationRulesDetailNode.name + " XXX"
                    def validationRulesItemsNode = validationRulesChildNode.children[1]
                    println validationRulesItemsNode.name + " XXX"            
                    validationRuleDetails = populateValidationRuleDetails(validationRulesDetailNode)
                    validationRuleItems = populateValidationRuleItems(validationRulesItemsNode)                        
                //}
                validationRuleItems.each{
                    validationRule.validationRuleItemss.add(it)
                }
                validationRule.validationRuleDetail = validationRuleDetails
                //println validationRuleDetails
                println "++++Adding a validation rule...++++"
                validationRules.add(validationRule)                
            //}
        }
        println "****Size:${validationRules.size()}****"
        return validationRules
    }

    ValidationRuleDetails populateValidationRuleDetails(validationRuleDetailsNode){
        ValidationRuleDetails validationRuleDetails = new ValidationRuleDetails()
        validationRuleDetailsNode.children.each{ validationRuleDetailsNodeChild ->
            switch(validationRuleDetailsNodeChild.name){
                case "code":
                    //validationRuleDetails.code = validationRuleDetailsNodeChild.text()
                    break
                case "description":
                    validationRuleDetails.description = validationRuleDetailsNodeChild.text()
                    break
                case "formula":
                    validationRuleDetails.formula = validationRuleDetailsNodeChild.text()
                    break
            }
        }
        return validationRuleDetails
    }

    Set<ValidationRuleItem> populateValidationRuleItems(validationRuleItemsNode){
        Set<ValidationRuleItem> validationRuleItems = new HashSet<ValidationRuleItem>()
        validationRuleItemsNode.children.each{ validationRuleItemsNodeChild ->
            ValidationRuleItem validationRuleItem = new ValidationRuleItem()
            validationRuleItemsNodeChild.children.each{ validationRuleItemNode ->
                //validationRuleItemNode.children.each{ validationRuleItemNodeChild ->
                    println "***" + validationRuleItemNode.name + "***"
                    //println validationRuleItemNodeChild.name
                    //println validationRuleItemNodeChild.children
                    
                    switch(validationRuleItemNode.name){
                        case "type":
                            validationRuleItem.type = validationRuleItemNode.text()
                            break
                        case "evaluate":
                            validationRuleItem.evaluate = validationRuleItemNode.text()
                            break
                        case "value":
                            validationRuleItem.value = validationRuleItemNode.text()
                            break
                    }
            }
            validationRuleItems.add(validationRuleItem)
        }
        println validationRuleItems
        return validationRuleItems
    }

Set<Heading> populateHeadings(headingsNode){
    Set<Heading> headings = new HashSet<Heading>()
    headingsNode.children.each{ headingsNodeChild ->
        println headingsNodeChild.children
        headingsNodeChild.each{ headingNodeChild ->
            Heading heading = new Heading() 
            headingNodeChild.children.each{ headingNodeChildChild ->
                   
                println headingNodeChildChild.name
                switch(headingNodeChildChild.name){
                    case "code":                        
                        heading.code = headingNodeChildChild.text()
                    break
                    case "description":   
                        heading.description = headingNodeChildChild.text() 
                    break
                    case "parent":    
                        heading.parent = headingNodeChildChild.text()
                    break
                    case "notes":    
                        heading.notes = headingNodeChildChild.text()
                    break
                    case "annotation":                                              
                        heading.annotation = headingNodeChildChild.text()
                    break      
                }
            }
            headings.add(heading)
        }
    }
    return headings
}

Set<Input> populateInputs(inputsNode){
    Set<Input> inputs = new HashSet<Input>()
    inputsNode.children.each{ inputNode ->
        Input input = new Input()    
        inputNode.children.each{ inputNodeChild ->
            switch(inputNodeChild.name){
                case "code":
                    input.code = inputNodeChild.text()
                break
                case "tag":
                    input.tag = inputNodeChild.text()                
                break
                case "version":
                    input.version = inputNodeChild.text()                
                break
                case "company":
                    input.company = inputNodeChild.text()                
                break 
                case "default":
                    input.defaultInput = new Boolean(inputNodeChild.text())                
                break                                                                
            }
        }
        inputs.add(input)    
    } 
    return inputs
}

Set<Item> populateItems(itemsNode){
    Set<Item> items = new HashSet<Item>()
    itemsNode.children.each{ itemNode ->
        Item item = new Item()
        itemNode.children.each{ itemNodeChild ->
            //println "name:" + itemNodeChild.name
            switch(itemNodeChild.name){
                case "code":
                    item.code = itemNodeChild.text()
                break
                case "version-number":
                    item.versionNumber = itemNodeChild.text()                
                break
                case "pricebase-code":
                    item.pricebaseCode = itemNodeChild.text()                
                break
                case "purpose-code":
                    item.purposeCode = itemNodeChild.text()                
                break
                case "text-code":
                    item.textCode = itemNodeChild.text()                
                break                                                                
            }
        }
        items.add(item)
   }
   return items
}

Set<Year> populateYears(yearsNode){
    Set<Year> years = new HashSet<Year>()
    yearsNode.children.each{ yearNode ->
        Year year = new Year()
        year.base = yearNode.text()
        years.add(year) 
    }
    return years
}

Set<Audit> populateAudits(auditsNode){
    Set<Audit> audits = new HashSet<Audit>()
    auditsNode.children.each{ auditNode ->
        Audit audit = new Audit()
        AuditDetails auditDetails = new AuditDetails()
        def auditDetailsNode = auditNode.children[0]
        auditDetailsNode.children.each{ auditDetailsChild ->
            //TODO add audit details data
            switch(auditDetailsChild.name){
                case "username":
                    auditDetails.username = auditDetailsChild.text()
                break
                case "timestamp":
                    auditDetails.timestamp = auditDetailsChild.text()
                break
                case "reason":
                    auditDetails.reason = auditDetailsChild.text()
                break                                
            }
            //auditDetailsChild.text()   
        }
        Set<AuditChange> auditChanges = new HashSet<AuditChange>()
        auditNode.children[1].children().each{ changeNode ->
            AuditChange change = new AuditChange()
            change.change = changeNode.children[0]
            auditChanges.add(change);
        }
        audit.auditDetail = auditDetails
        audit.changess = auditChanges 
        audits.add(audit)       
    }
    
    
    return audits
}

Model populateModelDetails(modelDetailsNode, modelNode){
    Model model = new Model()
    ModelDetails modelDetails = new ModelDetails()
    
    modelDetailsNode.children.each{ modelDetailsChild ->
        switch(modelDetailsChild.name){
            case "code":
                modelDetails.code = modelDetailsChild.text()
            break   
            case "name":
                modelDetails.name = modelDetailsChild.text()
            break   
            case "version":
                modelDetails.version = modelDetailsChild.text()
            break   
            case "type":
                modelDetails.type = modelDetailsChild.text()            
            break
            case "text-code":
                modelDetails.textCode = modelDetailsChild.text()            
            break      
            case "report-year-code":
                modelDetails.reportYearCode = modelDetailsChild.text()                        
            break                 
            case "base-year-code":
                modelDetails.baseYearCode = modelDetailsChild.text()                        
            break           
            case "allow-data-changes":
                modelDetails.allowDataChanges = new Boolean(modelDetailsChild.text())                        
            break                                                     
            case "model-family-code":
                modelDetails.modelFamilyCode = modelDetailsChild.text()                        
            break               
            case "model-family-parent":
                modelDetails.modelFamilyParent = new Boolean(modelDetailsChild.text())                        
            break               
            case "display-order":
                modelDetailsDisplayOrder = new Integer(modelDetailsChild.text())                        
            break              
            case "branch-tag":
                modelDetails.branchTag = modelDetailsChild.text()                        
            break   
            case "run-code":
                modelDetails.runCode = modelDetailsChild.text()                        
            break                            
        }       
        
    }
    //modelDetails.code = modelDetailsNode.
    model.modelDetails = modelDetails
    modelDetailsNode = null
    modelNode = null
    return model
} 

//println model.auditss[0].changess

return null