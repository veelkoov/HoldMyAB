let $rows = []
let $checkboxShowIssues
let $primarySearchField
let $additionalSearchRow

function shouldShowIssues() {
    return $checkboxShowIssues.is(':checked')
}

function rowMatchesAny($row, searchedTexts) {
    if (0 === searchedTexts.length) {
        return true
    }

    let items = $row.data('items')

    for (let searchedText of searchedTexts) {
        if (items.includes(searchedText)) {
            return true
        }
    }

    return false
}

function isIssuesRow($row) {
    return $row.is('.issues')
}

function unmarkMatches($row) {
    $row.find('.names span:not(.tag) span.found').each(function (idx, element) {
        $(element).replaceWith(element.innerHTML)
    })
}

function markMatches($row, searchedRegexes) {
    $row.find('.names span:not(.tag)').each(function (idx, element) {
        let oldHtml = element.innerHTML;
        let newHtml = oldHtml

        for (let regex of searchedRegexes) {
            newHtml = newHtml.replace(regex, '<span class="found">$&</span>')
        }

        if (newHtml !== oldHtml) {
            element.innerHTML = newHtml
        }
    })
}

function refreshVisibility() {
    const searchedTexts = $('input.searched-text')
        .map((index, element) => element.value.toLowerCase().trim())
        .toArray().filter(value => '' !== value)

    let searchedRegexes = []

    for (let text of searchedTexts) {
        searchedRegexes.push(new RegExp(text.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'gi'))
    }

    const showIssues = shouldShowIssues()

    for (const $row of $rows) {
        unmarkMatches($row)

        if (rowMatchesAny($row, searchedTexts) && (showIssues || !isIssuesRow($row))) {
            $row.show()

            markMatches($row, searchedRegexes)
        } else {
            $row.hide()
        }
    }
}

function initializeSearchInput($field, searchedText = '') {
    $field.on('keyup', function (event) {
        if (event.key === 'Enter') {
            refreshVisibility()
        }
    }).val(searchedText)
}

function getSearchDataFromHash() {
    let fallbackResult = {terms: []}
    let hash = window.location.hash.slice(1)

    if (!hash.startsWith("search:")) {
        console.log("Search setup: window.location.hash is empty or it doesn't start with `search:`")

        return fallbackResult
    }

    let decodedData = decodeURIComponent(hash.slice(7));
    console.log("Search setup: decoded value:", decodedData)

    fallbackResult = {terms: [decodedData]}

    if (!decodedData.startsWith('[') || !decodedData.endsWith(']')) {
        console.log("Search setup: treating decoded value as a plain string, as it doesn't start with `[` and/or end with `]`")

        return fallbackResult
    }

    let parsedData

    try {
        parsedData = JSON.parse(decodedData)
    } catch (ex) {
        console.log("Search setup: treating decoded value as a plain string, as it doesn't parse as JSON:", ex.message)

        return fallbackResult
    }

    if (!parsedData instanceof Array) {
        console.log("Search setup: treating decoded value as a plain string, as the parsed data is not an array")

        return fallbackResult
    }

    if (0 === parsedData.length) {
        console.log("Search setup: skipping, as the parsed data is an empty array")

        return fallbackResult
    }

    for (let idx in parsedData) {
        if ('string' !== typeof parsedData[idx]) {
            console.log(`Search setup: element ${idx} of the decoded array is not a string:`, parsedData[idx])

            return fallbackResult
        }
    }

    console.log("Search setup: successfully parsed decoded value")

    return {terms: parsedData}
}

function setupSearchBasedOnHash() {
    let searchData = getSearchDataFromHash()

    if (0 === searchData.terms.length) {
        return
    }

    $primarySearchField.val(searchData.terms[0])

    for (let idx = 1; idx < searchData.terms.length; idx++) {
        addNewSearchRow(searchData.terms[idx])
    }
}

function addNewSearchRow(searchedText = '') {
    let $newSearchRow = $additionalSearchRow.clone()

    $newSearchRow
        .removeClass('d-none')
        .removeClass('additional-search-template')
        .addClass('additional-search')
        .find('button').on('click', function (event) {
        $(event.target).parents('.additional-search').remove()
    })

    initializeSearchInput($newSearchRow.find('input'), searchedText)

    $additionalSearchRow.after($newSearchRow)
}

$(document).ready(function () {
    $additionalSearchRow = $('div.additional-search-template')

    $checkboxShowIssues = $('#checkboxShowIssues')
    $checkboxShowIssues.on('change', function () {
        refreshVisibility()
    })

    $primarySearchField = $('#primarySearchField')
    initializeSearchInput($primarySearchField)

    $('#searchButton').on('click', function () {
        refreshVisibility()
        $primarySearchField.focus()
    })

    $('#clearButton').on('click', function () {
        $primarySearchField.val('')
        $('div.additional-search').remove()

        refreshVisibility()
        $primarySearchField.focus()
    })

    $('#addSearch').on('click', () => addNewSearchRow())

    $('#recordsRows tr').each(function (rowIndex, rowElement) {
        let $row = $(rowElement)
        $rows.push($row)

        if (isIssuesRow($row)) {
            return
        }

        let names = $row.find('.names span:not(.tag)').map(function (nameIndex, nameElement) {
            return nameElement.innerHTML
        }).toArray().join("\n").toLowerCase()

        $row.data('items', names)
        $row.next().filter('.issues').data('items', names)
    })

    setupSearchBasedOnHash()
    refreshVisibility()
})
