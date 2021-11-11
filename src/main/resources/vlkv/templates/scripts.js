let $rows = []
let $checkboxShowIssues
let $searchValue

function shouldShowIssues() {
    return $checkboxShowIssues.is(':checked')
}

function rowMatches($row, searchedText) {
    if ('' === searchedText.trim()) {
        return true
    }

    return $row.data('items').includes(searchedText)
}

function isIssuesRow($row) {
    return $row.is('.issues')
}

function refreshVisibility() {
    const searchedText = $searchValue.val().toLowerCase()
    const showIssues = shouldShowIssues()

    for (const $row of $rows) {
        $row.toggle(rowMatches($row, searchedText) && (showIssues || !isIssuesRow($row)))
    }
}

$(document).ready(function () {
    let $searchButton = $('#searchButton')
    $checkboxShowIssues = $('#checkboxShowIssues')
    $searchValue = $('#searchValue')

    $checkboxShowIssues.on('change', function () {
        refreshVisibility()
    })
    $searchButton.on('click', function () {
        refreshVisibility()
        $searchValue.focus()
    })
    $('#clearButton').on('click', function () {
        $searchValue.val('')

        refreshVisibility()
        $searchValue.focus()
    })

    $searchValue.on('keyup', function (event) {
        if (event.key === 'Enter') {
            refreshVisibility()
        }
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
        $searchValue.val(decodeURI(hash.substr(7)))
    }

    refreshVisibility()
})
