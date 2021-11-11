let $rows = []
let $checkboxShowIssues
let $primarySearchField

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

function refreshVisibility() {
    const searchedTexts = $('input.searched-text')
        .map((index, element) => element.value.toLowerCase().trim())
        .toArray().filter(value => '' !== value)

    const showIssues = shouldShowIssues()

    for (const $row of $rows) {
        $row.toggle(rowMatchesAny($row, searchedTexts) && (showIssues || !isIssuesRow($row)))
    }
}

function enterInFieldCausesSearch($field) {
    $field.on('keyup', function (event) {
        if (event.key === 'Enter') {
            refreshVisibility()
        }
    })
}

$(document).ready(function () {
    $checkboxShowIssues = $('#checkboxShowIssues')
    $checkboxShowIssues.on('change', function () {
        refreshVisibility()
    })

    $primarySearchField = $('#primarySearchField')
    enterInFieldCausesSearch($primarySearchField)

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

    let $additionalSearchRow = $('div.additional-search-template')

    $('#addSearch').on('click', function () {
        let $newSearchRow = $additionalSearchRow.clone()

        $newSearchRow
            .removeClass('d-none')
            .removeClass('additional-search-template')
            .addClass('additional-search')
            .find('button').on('click', function (event) {
                $(event.target).parents('.additional-search').remove()
            })

        enterInFieldCausesSearch($newSearchRow.find('input'))

        $additionalSearchRow.after($newSearchRow)
    })

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

    let hash = window.location.hash.slice(1)

    if (hash.startsWith("search:")) {
        $primarySearchField.val(decodeURI(hash.substr(7)))
    }

    refreshVisibility()
})
