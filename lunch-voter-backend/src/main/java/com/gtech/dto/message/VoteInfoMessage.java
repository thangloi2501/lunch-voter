package com.gtech.dto.message;

import com.gtech.dto.api.VoteItem;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class VoteInfoMessage implements Message {

	private List<VoteItem> voteItems;

	@Override
	public Type getType() {
		return Type.VOTE_INFO;
	}
}