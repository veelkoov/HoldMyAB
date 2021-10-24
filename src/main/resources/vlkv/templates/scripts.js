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
    })

    $searchValue.on('keyup', function (event) {
        if (event.key === 'Enter') {
            refreshVisibility()
        }
    })

    $('#recordsRows tr').each(function (index, element) {
        let $row = $(element)
        $rows.push($row)

        if (isIssuesRow($row)) {
            return
        }

        let names = $row.find('.names span').map(function (index2, element2) {
            return element2.innerHTML
        }).toArray().join("\n").toLowerCase()

        $row.data('items', names)
        $row.next().filter('.issues').data('items', names)
    })

    refreshVisibility()
})
